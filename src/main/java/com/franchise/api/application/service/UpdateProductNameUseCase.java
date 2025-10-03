package com.franchise.api.application.service;

        import com.franchise.api.application.usecase.UpdateNameCommand;
        import com.franchise.api.application.usecase.ProductResponse;
        import com.franchise.api.domain.entity.Product;
        import com.franchise.api.domain.port.ProductRepository;
        import com.franchise.api.domain.exception.ProductNotFoundException;
        import org.springframework.stereotype.Service;
        import reactor.core.publisher.Mono;

        @Service
        public class UpdateProductNameUseCase {

            private final ProductRepository productRepository;

            public UpdateProductNameUseCase(ProductRepository productRepository) {
                this.productRepository = productRepository;
            }

            public Mono<ProductResponse> execute(String productId, UpdateNameCommand command) {
                return productRepository.findById(productId)
                    .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado: " + productId)))
                    .flatMap(product -> {
                        Product updatedProduct = product.updateName(command.name());
                        return productRepository.save(updatedProduct);
                    })
                    .map(ProductResponse::fromDomain);
            }
        }