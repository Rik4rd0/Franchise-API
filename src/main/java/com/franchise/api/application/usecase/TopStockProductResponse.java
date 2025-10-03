package com.franchise.api.application.usecase;

                             import com.franchise.api.domain.entity.Product;

                             public record TopStockProductResponse(
                                     String productId,
                                     String productName,
                                     int stock,
                                     String branchName // Debe ser String, no FranchiseName ni BranchName
                             ) {

                                 public static TopStockProductResponse fromDomain(Product product, String branchName) {
                                     return new TopStockProductResponse(
                                             product.id(),
                                             product.name().value(),
                                             product.stock().value(),
                                             branchName // Ya es String
                                     );
                                 }
                             }