package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.infrastructure.adapter.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SpringDataFranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
    Mono<FranchiseDocument> findByName(String name);
}