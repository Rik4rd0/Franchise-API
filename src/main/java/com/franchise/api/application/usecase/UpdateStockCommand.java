package com.franchise.api.application.usecase;

public record UpdateStockCommand(
        @jakarta.validation.constraints.NotNull(message = "El ID del producto no puede ser nulo")
        String productId,

        @jakarta.validation.constraints.Positive(message = "El stock debe ser positivo")
        Integer stock
) {
}