package com.aruiz.loans.loans.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Loan {
    private final List<Payment> payments;
    @JsonProperty("payment_id")
    private final int id;

    public Loan(int id, int terms) {
        payments = new ArrayList<>(terms);
        this.id = id;
    }

    public Loan(int id, List<Payment> payments) {
        this.id = id;
        this.payments = payments;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public int getId() {
        return id;
    }
}
