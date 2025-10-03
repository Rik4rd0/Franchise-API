package com.franchise.api.application.service;

import com.franchise.api.domain.port.ProductRepository;
import com.franchise.api.domain.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Void> execute(String productId) {
        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado: " + productId)))
            .flatMap(product -> productRepository.deleteById(productId));
    }
}

