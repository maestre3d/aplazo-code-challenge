package com.aruiz.loans.loans.application;

import com.aruiz.loans.loans.domain.InterestType;
import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.Payment;
import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.shared.domain.factory.RandomIntegerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository repository;
    private final int ID_FACTORY_MAX_BOUND = 10000;

    @Autowired
    public LoanService(LoanRepository repository) {
        this.repository = repository;
    }

    public Loan generateLoan(InterestType interestType, Double amount, int weekTerms, Double rate) throws Exception {
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
        // Using weekTerms/52 as t in the equation as formulas require years.
        double timeYear = weekTerms/52d;
        rate /= 100;
        // Simple interest formula:
        // A = P(1+rt)
        if (interestType == InterestType.SIMPLE) {
            return amount * (1 + rate*(timeYear));
        }

        double compoundPeriods = 1;
        switch (interestType) {
            case WEEKLY_COMPOUND -> compoundPeriods = 52;
            case MONTHLY_COMPOUND -> compoundPeriods = 12;
        }

        // Compound interest formula
        // A = P(1+r/n)^nt
        return amount * Math.pow(1 + rate/compoundPeriods, compoundPeriods*(timeYear));
    }
}
