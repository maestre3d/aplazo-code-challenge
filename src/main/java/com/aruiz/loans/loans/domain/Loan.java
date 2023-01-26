package com.aruiz.loans.loans.domain;

import java.util.ArrayList;
import java.util.List;

public class Loan {
    private final List<Payment> payments;
    private final int id;

    public Loan(int id, int terms) {
        payments = new ArrayList<>(terms);
        this.id = id;
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
