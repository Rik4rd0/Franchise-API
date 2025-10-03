package com.franchise.api.application.service;

import com.franchise.api.application.usecase.FranchiseResponse;
import com.franchise.api.application.usecase.UpdateNameCommand;
import com.franchise.api.domain.port.FranchiseRepository;
import com.franchise.api.domain.exception.FranchiseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository repository;

    public UpdateFranchiseNameUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, @Valid UpdateNameCommand command) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> franchise.updateName(command.name()))
                .flatMap(repository::save)
                .map(FranchiseResponse::fromDomain);
    }
}