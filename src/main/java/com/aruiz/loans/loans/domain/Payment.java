package com.aruiz.loans.loans.domain;

import java.time.LocalDate;

public record Payment(Integer id, Double amount, LocalDate paymentDate) {
}
