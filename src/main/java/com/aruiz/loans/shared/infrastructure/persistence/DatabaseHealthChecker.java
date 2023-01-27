package com.aruiz.loans.shared.infrastructure.persistence;

public interface DatabaseHealthChecker {
    boolean checkLiveness() throws Exception;
}
