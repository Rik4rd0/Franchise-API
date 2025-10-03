package com.franchise.api.application.usecase;

import com.franchise.api.domain.entity.Product;
import com.franchise.api.domain.vo.BranchName;

public record ProductWithBranch(
        Product product,
        BranchName branchName
) {
}