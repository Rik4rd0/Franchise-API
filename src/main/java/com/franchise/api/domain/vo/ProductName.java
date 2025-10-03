package com.franchise.api.domain.vo;

import com.franchise.api.domain.exception.DomainValidationException;

public record ProductName(String value) {
    public ProductName {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainValidationException("El nombre del producto no puede estar vacío.");
        }
        if (value.length() < 2) {
            throw new DomainValidationException("El nombre del producto debe tener al menos 2 caracteres.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}