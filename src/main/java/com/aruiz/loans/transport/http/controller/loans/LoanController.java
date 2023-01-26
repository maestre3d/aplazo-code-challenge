package com.aruiz.loans.transport.http.controller.loans;

import com.aruiz.loans.loans.application.LoanService;
import com.aruiz.loans.loans.domain.InterestType;
import com.aruiz.loans.loans.domain.Loan;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;

@RestController
public class LoanController {
    private final LoanService service;

    @Autowired
    public LoanController(LoanService service) {
        this.service = service;
    }

    private record GenerateLoanBody (@JsonAlias("interest_type") String interestType,
                                     Double amount, @JsonAlias("week_terms") int terms,
                                     Double rate) implements Serializable {
    }

    @PostMapping("/loans/generate")
    public Loan generateLoan(@RequestBody GenerateLoanBody request) throws Exception {
        return service.generateLoan(InterestType.valueOf(request.interestType), request.amount,
                request.terms, request.rate);
    }
}
