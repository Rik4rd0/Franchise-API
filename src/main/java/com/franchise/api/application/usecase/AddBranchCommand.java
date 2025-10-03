package com.franchise.api.application.usecase;

public record AddBranchCommand (String name, String franchiseId) {
    public AddBranchCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la sucursal no puede estar vacío");
        }
        if (franchiseId == null || franchiseId.isBlank()) {
            throw new IllegalArgumentException("El ID de la franquicia no puede estar vacío");
        }
    }
}
