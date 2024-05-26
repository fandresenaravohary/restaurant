package com.kfc.repository;

import java.util.List;

public interface CrudOperation<T, ID> {
    List<T> findAll();
    T getById(ID id);
    T save(T toSave);
    T deleteById(ID id);
    T updateById(ID id);
}