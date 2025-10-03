package com.franchise.api.application.service;

import com.franchise.api.application.usecase.AddFranchiseCommand;
import com.franchise.api.application.usecase.FranchiseResponse;
import com.franchise.api.domain.entity.Franchise;
import com.franchise.api.domain.exception.DuplicateFranchiseException;
import com.franchise.api.domain.port.FranchiseRepository;
import com.franchise.api.domain.vo.FranchiseName;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de Uso: Añadir una nueva Franquicia.
 * Lógica de aplicación que orquesta la validación de duplicados y la persistencia.
 */
@Service
public class AddFranchiseUseCase {

    private final FranchiseRepository repository;

    public AddFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta el caso de uso para crear una nueva franquicia.
     *
     * @param command El comando que contiene el nombre de la franquicia.
     * @return Mono que emite la respuesta de la franquicia creada (FranchiseResponse).
     */
    public Mono<FranchiseResponse> execute(AddFranchiseCommand command) {
        return repository.findByName(new FranchiseName(command.name()))
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateFranchiseException(command.name()));
                    }
                    Franchise newFranchise = Franchise.createNew(command.name());
                    return repository.save(newFranchise);
                })
                .map(FranchiseResponse::fromDomain);
    }
}