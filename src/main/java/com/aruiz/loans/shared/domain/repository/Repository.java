package com.aruiz.loans.shared.domain.repository;

import java.util.List;

public interface Repository<T> {
    void save(T record) throws Exception;
    T getById(int id) throws Exception;
}
