package com.franchise.api.application.usecase;

public record AddFranchiseCommand(String name) {
    public AddFranchiseCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la franquicia no puede estar vacío");
        }
    }
}
