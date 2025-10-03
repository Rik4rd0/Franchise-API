package com.franchise.api.application.usecase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddProductCommand(
    @NotBlank(message = "El nombre del producto es obligatorio")
    String name,

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    Integer stock
) {}