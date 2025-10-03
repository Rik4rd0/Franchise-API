package com.franchise.api.domain.port;

    import com.franchise.api.domain.entity.Product;
    import com.franchise.api.domain.vo.ProductName;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;

    public interface ProductRepository {

        Mono<Product> save(Product product);

        Mono<Product> findById(String id);

        Flux<Product> findAll();

        Mono<Void> deleteById(String id);

        Mono<Product> findByName(ProductName name);

        Flux<Product> findByBranchId(String branchId);
    }