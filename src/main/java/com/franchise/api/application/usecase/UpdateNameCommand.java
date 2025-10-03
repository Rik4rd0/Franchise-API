package com.franchise.api.application.usecase;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameCommand(
    @NotBlank(message = "Name cannot be blank")
    String name
) {
}