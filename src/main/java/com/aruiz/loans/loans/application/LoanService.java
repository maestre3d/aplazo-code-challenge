package com.aruiz.loans.loans.application;

import com.aruiz.loans.loans.domain.InterestType;
import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.Payment;
import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.shared.domain.factory.RandomIntegerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.List;

public class LoanService {
    private final LoanRepository repository;
    private final int ID_FACTORY_MAX_BOUND = 10000;

    public LoanService(LoanRepository repository) {
        this.repository = repository;
    }

    public Loan generateLoan(InterestType interestType, Double amount, int weekTerms, Double rate) {
        if (weekTerms < 4)
            weekTerms = 4; // use as default a month
        else if (weekTerms > 52)
            weekTerms = 52; // use as default a year

        Loan loan = new Loan(RandomIntegerFactory.generateId(ID_FACTORY_MAX_BOUND), weekTerms);

        LocalDate now = LocalDate.now();
        List<LocalDate> paymentTerms = now.datesUntil(LocalDate.now().plusWeeks(weekTerms), Period.ofWeeks(1)).toList();
        Iterator<LocalDate> termIterator = paymentTerms.iterator();

        double paymentAmount = calculateInterest(interestType, amount, weekTerms, rate) / weekTerms;
        int count = 1;
        while (termIterator.hasNext()) {
            loan.addPayment(new Payment(count++, paymentAmount, termIterator.next()));
        }

        repository.save(loan);
        return loan;
    }

    private double calculateInterest(InterestType interestType, Double amount, int weekTerms, Double rate) {
        rate /= 100;
        // Simple interest formula:
        // A = P(1+rt)
        if (interestType == InterestType.SIMPLE)
            return amount * (1 + rate*(weekTerms/52d));

        double compoundPeriods = 1;
        switch (interestType) {
            case WEEKLY_COMPOUND -> compoundPeriods = 52;
            case MONTHLY_COMPOUND -> compoundPeriods = 12;
        }

        // Compound interest formula
        // A = P(1+r/n)^nt
        // Using weekTerms/52 as t in equation as formula requires years.
        return amount * Math.pow(1 + rate/compoundPeriods, compoundPeriods*(weekTerms/52d));
    }
}
