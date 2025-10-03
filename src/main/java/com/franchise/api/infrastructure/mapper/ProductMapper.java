package com.franchise.api.infrastructure.mapper;

import com.franchise.api.domain.entity.Product;
import com.franchise.api.domain.vo.ProductName;
import com.franchise.api.domain.vo.Stock;
import com.franchise.api.infrastructure.adapter.document.ProductDocument;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDocument toDocument(Product product) {
        return new ProductDocument(
                product.id(),
                product.name().value(),
                product.stock().value(),
                product.branchId()
        );
    }

    public Product toEntity(ProductDocument document) {
        return new Product(
                document.getId(),
                new ProductName(document.getName()),
                new Stock(document.getStock()),
                document.getBranchId()
        );
    }
}