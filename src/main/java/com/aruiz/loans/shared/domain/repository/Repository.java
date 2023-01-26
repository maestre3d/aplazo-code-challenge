package com.aruiz.loans.shared.domain.repository;

import com.aruiz.loans.shared.domain.exceptions.ItemNotFoundException;

import java.util.List;

public interface Repository<T> {
    void save(T record);
    List<T> list() throws ItemNotFoundException;
}
