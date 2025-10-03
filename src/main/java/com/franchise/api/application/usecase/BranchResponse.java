package com.franchise.api.application.usecase;

public record BranchResponse(String id, String name, String franchiseId) {
    public static BranchResponse fromDomain(com.franchise.api.domain.entity.Branch branch) {
        return new BranchResponse(
                branch.id(),
                branch.name().value(),
                branch.franchiseId()
        );
    }
}
