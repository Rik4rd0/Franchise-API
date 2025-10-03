package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.infrastructure.adapter.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpringDataProductRepository extends ReactiveMongoRepository<ProductDocument, String> {

    Mono<ProductDocument> findByName(String name);

    Flux<ProductDocument> findByBranchId(String branchId);
}