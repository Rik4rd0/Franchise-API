package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.infrastructure.adapter.document.BranchDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpringDataBranchRepository extends ReactiveMongoRepository<BranchDocument, String> {

    Mono<BranchDocument> findByName(String name);

    Flux<BranchDocument> findByFranchiseId(String franchiseId);
}