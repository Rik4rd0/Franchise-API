package com.franchise.api.domain.vo;

import com.franchise.api.domain.exception.DomainValidationException;

public record BranchName(String value) {

    public BranchName {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainValidationException("El nombre de la sucursal no puede estar vacío.");
        }
        if (value.length() < 2) {
            throw new DomainValidationException("El nombre de la sucursal debe tener al menos 2 caracteres.");
        }
        if (value.length() > 100) {
            throw new DomainValidationException("El nombre de la sucursal no puede exceder 100 caracteres.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}