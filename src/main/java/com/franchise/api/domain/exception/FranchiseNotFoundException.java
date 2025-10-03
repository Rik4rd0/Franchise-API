package com.franchise.api.domain.exception;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(String id) {
        super("Franquicia no encontrada con ID: " + id);
    }
}