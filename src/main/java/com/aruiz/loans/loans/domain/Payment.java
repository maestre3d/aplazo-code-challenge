package com.aruiz.loans.loans.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public record Payment(@JsonProperty("payment_number") Integer paymentNumber, Double amount,
                      @JsonProperty("payment_date") LocalDate paymentDate) implements Serializable {
}
