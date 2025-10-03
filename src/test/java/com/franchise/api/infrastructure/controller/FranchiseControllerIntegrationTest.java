package com.franchise.api.infrastructure.controller;

import com.franchise.api.application.usecase.AddFranchiseCommand;
import com.franchise.api.domain.port.FranchiseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@DisplayName("Tests de integración del FranchiseController")
class FranchiseControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withExposedPorts(27017);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        StepVerifier.create(franchiseRepository.findAll().flatMap(franchise -> 
                franchiseRepository.deleteById(franchise.id())))
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void shouldCreateFranchiseSuccessfully() throws Exception {
        // Given
        AddFranchiseCommand command = new AddFranchiseCommand("McDonald's");
        String requestBody = objectMapper.writeValueAsString(command);

        // When & Then
        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.name").isEqualTo("McDonald's")
            .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("Debe fallar al crear franquicia con nombre duplicado")
    void shouldFailWhenCreatingDuplicateFranchise() throws Exception {
        // Given
        AddFranchiseCommand command = new AddFranchiseCommand("KFC");
        String requestBody = objectMapper.writeValueAsString(command);

        // Crear la primera franquicia
        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isCreated();

        // When & Then - Intentar crear la segunda con el mismo nombre
        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar campos requeridos al crear franquicia")
    void shouldValidateRequiredFieldsWhenCreatingFranchise() {
        // Given - Comando con nombre vacío
        String invalidRequestBody = "{\"name\":\"\"}";

        // When & Then
        webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidRequestBody)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe agregar sucursal a franquicia existente")
    void shouldAddBranchToExistingFranchise() throws Exception {
        // Given - Crear franquicia primero
        AddFranchiseCommand franchiseCommand = new AddFranchiseCommand("Burger King");
        String franchiseRequestBody = objectMapper.writeValueAsString(franchiseCommand);

        String franchiseId = webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(franchiseRequestBody)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(String.class)
            .getResponseBody()
            .map(response -> {
                try {
                    return objectMapper.readTree(response).get("id").asText();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .blockFirst();

        // Crear comando para agregar sucursal
        String branchRequestBody = "{\"name\":\"Sucursal Centro\"}";

        // When & Then
        webTestClient.post()
            .uri("/api/v1/franchises/{franchiseId}/branches", franchiseId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(branchRequestBody)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Sucursal Centro")
            .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("Debe fallar al agregar sucursal a franquicia inexistente")
    void shouldFailWhenAddingBranchToNonExistentFranchise() {
        // Given
        String nonExistentFranchiseId = "non-existent-id";
        String branchRequestBody = "{\"name\":\"Sucursal Norte\"}";

        // When & Then
        webTestClient.post()
            .uri("/api/v1/franchises/{franchiseId}/branches", nonExistentFranchiseId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(branchRequestBody)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Debe obtener productos con mayor stock por franquicia")
    void shouldGetTopStockProductsByFranchise() throws Exception {
        // Given - Crear estructura completa
        AddFranchiseCommand franchiseCommand = new AddFranchiseCommand("Pizza Hut");
        String franchiseRequestBody = objectMapper.writeValueAsString(franchiseCommand);

        String franchiseId = webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(franchiseRequestBody)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(String.class)
            .getResponseBody()
            .map(response -> {
                try {
                    return objectMapper.readTree(response).get("id").asText();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .blockFirst();

        // When & Then
        webTestClient.get()
            .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchiseId)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Object.class);
    }

    @Test
    @DisplayName("Debe actualizar nombre de franquicia")
    void shouldUpdateFranchiseName() throws Exception {
        // Given - Crear franquicia
        AddFranchiseCommand franchiseCommand = new AddFranchiseCommand("Dominos");
        String franchiseRequestBody = objectMapper.writeValueAsString(franchiseCommand);

        String franchiseId = webTestClient.post()
            .uri("/api/v1/franchises")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(franchiseRequestBody)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(String.class)
            .getResponseBody()
            .map(response -> {
                try {
                    return objectMapper.readTree(response).get("id").asText();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .blockFirst();

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
