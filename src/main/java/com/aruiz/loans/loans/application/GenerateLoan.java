package com.aruiz.loans.loans.application;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.hibernate.validator.constraints.Range;

public record GenerateLoan (@JsonAlias("interest_type") String interestType,
                            @Range(min = 1, max = 999999, message = "amount out of range [1,999999]") Double amount,
                            @Range(min = 4, max = 52, message = "week_terms out of range [4,52]") @JsonAlias("week_terms") int weekTerms,
                            @Range(min = 1, max = 100, message = "rate out of range [1,100]") Double rate){}
