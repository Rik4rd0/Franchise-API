package com.franchise.api.application.service;

import com.franchise.api.application.usecase.AddProductCommand;
import com.franchise.api.application.usecase.ProductResponse;
import com.franchise.api.domain.entity.Product;
import com.franchise.api.domain.port.ProductRepository;
import com.franchise.api.domain.port.BranchRepository;
import com.franchise.api.domain.exception.BranchNotFoundException;
import com.franchise.api.domain.vo.Stock;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public AddProductUseCase(ProductRepository productRepository, BranchRepository branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public Mono<ProductResponse> execute(String branchId, AddProductCommand command) {
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new BranchNotFoundException("Sucursal no encontrada: " + branchId)))
            .flatMap(branch -> {
                Product product = Product.createNew(command.name(), command.stock(), branchId);
                return productRepository.save(product);
            })
            .map(product -> new ProductResponse(
                product.id(),
                product.name().value(),
                new Stock(product.stock().value())
            ));
    }
}