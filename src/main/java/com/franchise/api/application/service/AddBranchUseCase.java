package com.franchise.api.application.service;

import com.franchise.api.application.usecase.AddBranchCommand;
import com.franchise.api.application.usecase.BranchResponse;
import com.franchise.api.domain.exception.FranchiseNotFoundException;
import com.franchise.api.domain.port.BranchRepository;
import com.franchise.api.domain.entity.Branch;
import com.franchise.api.domain.port.FranchiseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddBranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public AddBranchUseCase(
            @Qualifier("mongoBranchRepository") BranchRepository branchRepository,
            FranchiseRepository franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<BranchResponse> execute(AddBranchCommand command) {
        return franchiseRepository.findById(command.franchiseId())
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada: " + command.franchiseId())))
                .flatMap(franchise -> {
                    Branch branch = Branch.createNew(command.name(),command.franchiseId());
                    return branchRepository.save(branch);
                })
                .map(BranchResponse::fromDomain);
    }
}