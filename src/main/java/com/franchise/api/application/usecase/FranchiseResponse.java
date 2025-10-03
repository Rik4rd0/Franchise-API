package com.franchise.api.application.usecase;

import com.franchise.api.domain.entity.Franchise;

/**
 * DTO de salida que representa la información de la franquicia creada.
 */
public record FranchiseResponse(String id, String name) {

    /**
     * Crea un DTO de respuesta a partir de la entidad de Dominio Franchise.
     * @param franchise La entidad de Dominio inmutable.
     * @return El DTO de respuesta.
     */
    public static FranchiseResponse fromDomain(Franchise franchise) {
        return new FranchiseResponse(
                franchise.id(),
                franchise.name().value()
        );
    }
}
