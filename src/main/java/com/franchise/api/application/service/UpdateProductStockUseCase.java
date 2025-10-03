package com.franchise.api.application.service;

import com.franchise.api.application.usecase.ProductResponse;
import com.franchise.api.application.usecase.UpdateStockCommand;
import com.franchise.api.domain.port.ProductRepository;
import com.franchise.api.domain.exception.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    public UpdateProductStockUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ProductResponse> execute(String productId, @Valid UpdateStockCommand command) {
        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado: " + productId)))
            .flatMap(product -> {
                product.updateStock(command.stock());
                return productRepository.save(product);
            })
            .map(ProductResponse::fromDomain);
    }
}

