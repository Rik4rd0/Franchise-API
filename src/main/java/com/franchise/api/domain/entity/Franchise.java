package com.franchise.api.domain.entity;

import com.franchise.api.domain.exception.BusinessRuleException;
import com.franchise.api.domain.vo.FranchiseName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entidad de Dominio: Franquicia
 * Contiene reglas para la gestión de sucursales.
 */
public record Franchise(
        String id,
        FranchiseName name,
        List<Branch> branches
) {
    public Franchise {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la franquicia no puede ser nulo.");
        }
        if (name == null) {
            throw new IllegalArgumentException("El nombre de la franquicia no puede ser nulo.");
        }
        branches = branches != null ? List.copyOf(branches) : List.of();
    }

    /**
     * Crea una nueva Franquicia.
     */
    public static Franchise createNew(String name) {
        return new Franchise(
                UUID.randomUUID().toString(),
                new FranchiseName(name),
                List.of()
        );
    }

    /**
     * Comportamiento de negocio: Añade una sucursal y devuelve una nueva instancia de Franchise.
     */
    public Franchise addBranch(Branch branch) {
        if (!branch.franchiseId().equals(this.id)) {
            throw new BusinessRuleException("La sucursal no pertenece a esta franquicia.");
        }

        List<Branch> newBranches = new ArrayList<>(this.branches);
        newBranches.add(branch);

        return new Franchise(this.id, this.name, newBranches);
    }

    /**
     * Comportamiento de negocio: Actualiza el nombre de la franquicia.
     */
    public Franchise updateName(String newName) {
        return new Franchise(this.id, new FranchiseName(newName), this.branches);
    }

    /**
     * Comportamiento de negocio: Busca el producto con más stock en cada sucursal.
     */
    public List<Product> findMaxStockedProductPerBranch() {
        return this.branches.stream()
                .map(Branch::findMostStockedProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para reemplazar una sucursal modificada.
     */
    public Franchise replaceBranch(Branch updatedBranch) {
        List<Branch> updatedBranches = this.branches.stream()
                .map(b -> b.id().equals(updatedBranch.id()) ? updatedBranch : b)
                .collect(Collectors.toList());

        boolean branchFound = this.branches.stream()
                .anyMatch(b -> b.id().equals(updatedBranch.id()));

        if (!branchFound) {
            throw new BusinessRuleException("No se pudo encontrar la sucursal con ID " + updatedBranch.id() + " para reemplazar.");
        }

        return new Franchise(this.id, this.name, updatedBranches);
    }

    /**
     * Elimina una sucursal por ID.
     */
    public Franchise removeBranch(String branchId) {
        List<Branch> filteredBranches = this.branches.stream()
                .filter(b -> !b.id().equals(branchId))
                .collect(Collectors.toList());

        return new Franchise(this.id, this.name, filteredBranches);
    }
}