package com.franchise.api.service;

import com.franchise.api.application.service.AddFranchiseUseCase;
import com.franchise.api.application.usecase.AddFranchiseCommand;
import com.franchise.api.application.usecase.FranchiseResponse;
import com.franchise.api.domain.entity.Franchise;
import com.franchise.api.domain.exception.DuplicateFranchiseException;
import com.franchise.api.domain.port.FranchiseRepository;
import com.franchise.api.domain.vo.FranchiseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del caso de uso AddFranchiseUseCase")
class AddFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private AddFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddFranchiseUseCase(franchiseRepository);
    }

    @Test
    @DisplayName("Debe crear franquicia exitosamente cuando no existe")
    void shouldCreateFranchiseSuccessfully() {
        // Given
        String franchiseName = "McDonald's";
        AddFranchiseCommand command = new AddFranchiseCommand(franchiseName);
        Franchise savedFranchise = Franchise.createNew(franchiseName);
        
        when(franchiseRepository.findByName(any(FranchiseName.class)))
            .thenReturn(Mono.empty());
        when(franchiseRepository.save(any(Franchise.class)))
            .thenReturn(Mono.just(savedFranchise));

        // When & Then
        StepVerifier.create(useCase.execute(command))
            .expectNextMatches(response -> 
                response.name().equals(franchiseName) &&
                response.id() != null
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar cuando la franquicia ya existe")
    void shouldFailWhenFranchiseAlreadyExists() {
        // Given
        String franchiseName = "KFC";
        AddFranchiseCommand command = new AddFranchiseCommand(franchiseName);
        Franchise existingFranchise = Franchise.createNew(franchiseName);
        
        when(franchiseRepository.findByName(any(FranchiseName.class)))
            .thenReturn(Mono.just(existingFranchise));

        // When & Then
        StepVerifier.create(useCase.execute(command))
            .expectErrorMatches(throwable ->
                throwable instanceof DuplicateFranchiseException &&
                throwable.getMessage().contains(franchiseName)
            )
            .verify();
    }

    @Test
    @DisplayName("Debe validar que el comando tenga el nombre correcto")
    void shouldValidateCommandName() {
        // Given
        String franchiseName = "Burger King";
        AddFranchiseCommand command = new AddFranchiseCommand(franchiseName);
        
        when(franchiseRepository.findByName(argThat(name -> name.value().equals(franchiseName))))
            .thenReturn(Mono.empty());
        when(franchiseRepository.save(any(Franchise.class)))
            .thenReturn(Mono.just(Franchise.createNew(franchiseName)));

        // When & Then
        StepVerifier.create(useCase.execute(command))
            .expectNextMatches(response -> response.name().equals(franchiseName))
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe propagar errores del repositorio durante la consulta")
    void shouldPropagateRepositoryErrorsDuringQuery() {
        // Given
        AddFranchiseCommand command = new AddFranchiseCommand("Pizza Hut");
        RuntimeException repositoryError = new RuntimeException("Error de conexión");
        
        when(franchiseRepository.findByName(any(FranchiseName.class)))
            .thenReturn(Mono.error(repositoryError));

        // When & Then
        StepVerifier.create(useCase.execute(command))
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    @DisplayName("Debe propagar errores del repositorio durante el guardado")
    void shouldPropagateRepositoryErrorsDuringSave() {
        // Given
        AddFranchiseCommand command = new AddFranchiseCommand("Subway");
        RuntimeException saveError = new RuntimeException("Error al guardar");
        
        when(franchiseRepository.findByName(any(FranchiseName.class)))
            .thenReturn(Mono.empty());
        when(franchiseRepository.save(any(Franchise.class)))
            .thenReturn(Mono.error(saveError));

        // When & Then
        StepVerifier.create(useCase.execute(command))
            .expectError(RuntimeException.class)
            .verify();
    }
}
