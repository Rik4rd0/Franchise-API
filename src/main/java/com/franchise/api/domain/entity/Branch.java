package com.franchise.api.domain.entity;

import com.franchise.api.domain.exception.BusinessRuleException;
import com.franchise.api.domain.vo.BranchName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de Dominio: Sucursal
 * Contiene reglas para la gestión de productos.
 */
public record Branch(
        String id,
        BranchName name,
        String franchiseId,
        List<Product> products
) {
    public Branch {
        Objects.requireNonNull(id, "El ID de la sucursal no puede ser nulo.");
        Objects.requireNonNull(name, "El nombre de la sucursal no puede ser nulo.");
        Objects.requireNonNull(franchiseId, "El ID de la franquicia no puede ser nulo.");
        products = products != null ? List.copyOf(products) : List.of();
    }

    /**
     * Crea una nueva Sucursal.
     */
    public static Branch createNew(String name, String franchiseId) {
        return new Branch(
                UUID.randomUUID().toString(),
                new BranchName(name),
                franchiseId,
                List.of()
        );
    }

    /**
     * Comportamiento de negocio: Añade un producto y devuelve una nueva instancia de Branch.
     */
    public Branch addProduct(Product product) {
        // Validación de negocio: Asegurar que el producto pertenezca a esta sucursal
        if (!product.branchId().equals(this.id)) {
            throw new BusinessRuleException("El producto no pertenece a esta sucursal.");
        }

        List<Product> newProducts = new ArrayList<>(this.products);
        newProducts.add(product);

        return new Branch(this.id, this.name, this.franchiseId, newProducts);
    }

    /**
     * Comportamiento de negocio: Actualiza el nombre de la sucursal.
     */
    public Branch updateName(String newName) {
        return new Branch(this.id, new BranchName(newName), this.franchiseId, this.products);
    }

    /**
     * Comportamiento de negocio: Busca el producto con más stock.
     */
    public Product findMostStockedProduct() {
        return this.products.stream()
                .max((p1, p2) -> Integer.compare(p1.stock().value(), p2.stock().value()))
                .orElse(null);
    }

    /**
     * Método auxiliar para reemplazar un producto modificado.
     */
    public Branch replaceProduct(Product updatedProduct) {
        List<Product> updatedProducts = this.products.stream()
                .map(p -> p.id().equals(updatedProduct.id()) ? updatedProduct : p)
                .toList();

        return new Branch(this.id, this.name, this.franchiseId, updatedProducts);
    }

    /**
     * Elimina un producto por ID.
     */
    public Branch removeProduct(String productId) {
        List<Product> filteredProducts = this.products.stream()
                .filter(p -> !p.id().equals(productId))
                .toList();

        return new Branch(this.id, this.name, this.franchiseId, filteredProducts);
    }


}