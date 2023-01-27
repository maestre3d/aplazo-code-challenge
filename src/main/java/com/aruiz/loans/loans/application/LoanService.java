package com.aruiz.loans.loans.application;

import com.aruiz.loans.loans.domain.InterestType;
import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.Payment;
import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.shared.domain.exceptions.ItemNotFoundException;
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

    public Loan generateLoan(GenerateLoan args) throws Exception {
        InterestType interestType = InterestType.valueOf(args.interestType());

        Loan loan = new Loan(RandomIntegerFactory.generateId(ID_FACTORY_MAX_BOUND), args.weekTerms());

        LocalDate now = LocalDate.now();
        List<LocalDate> paymentTerms = now.datesUntil(LocalDate.now().plusWeeks(args.weekTerms()), Period.ofWeeks(1)).toList();
        Iterator<LocalDate> termIterator = paymentTerms.iterator();

        double paymentAmount = calculateInterest(interestType, args.amount(), args.weekTerms(), args.rate()) / args.weekTerms();
        int count = 1;
        while (termIterator.hasNext()) {
            loan.addPayment(new Payment(count++, paymentAmount, termIterator.next()));
        }

        repository.save(loan);
        return loan;
    }

    public Loan getLoan(int loanId) throws Exception {
        Loan loan  = repository.getById(loanId);
        if (loan == null) {
            throw new ItemNotFoundException("loan");
        }
        return loan;
    }
}
