package com.franchise.api.domain.exception;

public class DuplicateFranchiseException extends RuntimeException {
    public DuplicateFranchiseException(String name) {
        super("Ya existe una franquicia con el nombre: " + name);
    }
}