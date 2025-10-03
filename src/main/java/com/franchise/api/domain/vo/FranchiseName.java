package com.franchise.api.domain.vo;

import com.franchise.api.domain.exception.DomainValidationException;

public record FranchiseName(String value) {
    public FranchiseName {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainValidationException("El nombre de la franquicia/sucursal no puede estar vacío.");
        }
        if (value.length() < 3) {
            throw new DomainValidationException("El nombre debe tener al menos 3 caracteres.");
        }
    }


    @Override
    public String toString() {
        return value;
    }
}

