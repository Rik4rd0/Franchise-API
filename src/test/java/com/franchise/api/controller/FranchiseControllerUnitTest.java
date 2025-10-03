package com.franchise.api.controller;

import com.franchise.api.application.service.*;
import com.franchise.api.infrastructure.controller.FranchiseController;
import com.franchise.api.application.usecase.AddFranchiseCommand;
import com.franchise.api.application.usecase.AddBranchCommand;
import com.franchise.api.application.usecase.UpdateNameCommand;
import com.franchise.api.application.usecase.FranchiseResponse;
import com.franchise.api.application.usecase.BranchResponse;
import com.franchise.api.application.usecase.TopStockProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Tests unitarios del FranchiseController")
class FranchiseControllerUnitTest {


    @Mock
    private AddFranchiseUseCase addFranchiseUseCase;

    @Mock
    private GetTopStockProductsByFranchiseUseCase getTopStockUseCase;

    @Mock
    private AddBranchUseCase addBranchUseCase;

    @Mock
    private AddProductUseCase addProductUseCase;

    @Mock
    private DeleteProductUseCase deleteProductUseCase;

    @Mock
    private UpdateProductStockUseCase updateStockUseCase;

    @Mock
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @Mock
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    @Mock
    private UpdateProductNameUseCase updateProductNameUseCase;

    private WebTestClient webTestClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        FranchiseController controller = new FranchiseController(
            addFranchiseUseCase,
            getTopStockUseCase,
            addBranchUseCase,
            addProductUseCase,
            deleteProductUseCase,
            updateStockUseCase,
            updateFranchiseNameUseCase,
            updateBranchNameUseCase,
            updateProductNameUseCase
        );

        this.webTestClient = WebTestClient.bindToController(controller).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void shouldCreateFranchiseSuccessfully() throws Exception {
        AddFranchiseCommand command = new AddFranchiseCommand("McDonald's");
        FranchiseResponse expectedResponse = new FranchiseResponse("franchise-123", "McDonald's");

        when(addFranchiseUseCase.execute(any(AddFranchiseCommand.class)))
            .thenReturn(Mono.just(expectedResponse));

        String requestBody = objectMapper.writeValueAsString(command);

        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.name").isEqualTo("McDonald's")
            .jsonPath("$.id").isEqualTo("franchise-123");
    }

    @Test
    @DisplayName("Debe devolver 409 al crear franquicia con nombre duplicado")
    void shouldReturn409WhenCreatingDuplicateFranchise() throws Exception {
        AddFranchiseCommand command = new AddFranchiseCommand("KFC");

        when(addFranchiseUseCase.execute(any(AddFranchiseCommand.class)))
            .thenReturn(Mono.error(new ResponseStatusException(
                org.springframework.http.HttpStatus.CONFLICT,
                "Ya existe una franquicia con el nombre: KFC")));

        String requestBody = objectMapper.writeValueAsString(command);

        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isEqualTo(409);
    }

    @Test
    @DisplayName("Debe devolver 400 para campos inválidos")
    void shouldReturn400ForInvalidFields() {
        String invalidRequestBody = "{\"name\":\"\"}";

        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidRequestBody)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe agregar sucursal a franquicia existente")
    void shouldAddBranchToExistingFranchise() {
        String franchiseId = "franchise-123";
        String branchName = "Sucursal Centro";
        AddBranchCommand command = new AddBranchCommand(franchiseId, branchName);
        BranchResponse expectedResponse = new BranchResponse("branch-456", branchName, franchiseId);

        when(addBranchUseCase.execute(any(AddBranchCommand.class)))
                .thenReturn(Mono.just(expectedResponse));

        webTestClient.post()
                .uri("/api/v1/franchises/{franchiseId}/branches", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BranchResponse.class)
                .isEqualTo(expectedResponse);
    }

  @Test
@DisplayName("Debe devolver 400 cuando falla agregar sucursal")
void shouldReturn400WhenAddingBranchFails() {
    // Given
    String franchiseId = "non-existent-id";
    String branchName = "Sucursal Norte";

    when(addBranchUseCase.execute(any(AddBranchCommand.class)))
            .thenReturn(Mono.error(new IllegalArgumentException("Franquicia no encontrada")));

    webTestClient.post()
            .uri("/api/v1/franchises/{franchiseId}/branches", franchiseId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("name", branchName)) // Usar Map en lugar de String manual
            .exchange()
            .expectStatus().isBadRequest();
}
    @Test
    @DisplayName("Debe obtener productos con mayor stock por franquicia")
    void shouldGetTopStockProductsByFranchise() {
        String franchiseId = "franchise-123";
            TopStockProductResponse product1 = new TopStockProductResponse(
                "product-1", "Pizza Grande", 50, "Sucursal Centro"
            );
            TopStockProductResponse product2 = new TopStockProductResponse(
                "product-2", "Pizza Mediana", 30, "Sucursal Norte"
            );
        when(getTopStockUseCase.execute(franchiseId))
            .thenReturn(Flux.just(product1, product2));

        webTestClient.get()
            .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchiseId)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(TopStockProductResponse.class)
            .hasSize(2);
    }

    @Test
    @DisplayName("Debe actualizar nombre de franquicia")
    void shouldUpdateFranchiseName() {
        // Given
        String franchiseId = "franchise-123";
        FranchiseResponse expectedResponse = new FranchiseResponse(franchiseId, "Domino's Pizza");

        when(updateFranchiseNameUseCase.execute(anyString(), any(UpdateNameCommand.class)))
            .thenReturn(Mono.just(expectedResponse));

        String updateRequestBody = "{\"name\":\"Domino's Pizza\"}";

        // When & Then
        webTestClient.patch()
            .uri("/api/v1/franchises/{franchiseId}/name", franchiseId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateRequestBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Domino's Pizza")
            .jsonPath("$.id").isEqualTo(franchiseId);
    }
}