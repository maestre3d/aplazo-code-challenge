package com.aruiz.loans.transport.http.controller.loans;

import com.aruiz.loans.loans.application.GenerateLoan;
import com.aruiz.loans.loans.application.LoanService;
import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.shared.domain.exceptions.ItemNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService service;

    @Autowired
    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan generateLoan(@RequestBody @Valid GenerateLoan request) throws Exception {
        try {
            return service.generateLoan(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "interest_type has an invalid format, expected [SIMPLE,ANNUALLY_COMPOUND,WEEKLY_COMPOUND,MONTHLY_COMPOUND]");
        }
    }

    @GetMapping("/{loan_id}")
    public Loan getLoanById(@PathVariable(value = "loan_id") String loanId) throws Exception {
        try {
            return service.getLoan(Integer.parseInt(loanId));
        } catch (ItemNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.toString(), ex);
        }
    }
}
