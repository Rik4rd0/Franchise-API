package com.franchise.api.infrastructure.controller;

import com.franchise.api.application.usecase.*;
import com.franchise.api.application.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/franchises")
@Tag(name = "Franchise Management", description = "API para gestión de franquicias, sucursales y productos")
public class FranchiseController {

    private final AddFranchiseUseCase addFranchiseUseCase;
    private final GetTopStockProductsByFranchiseUseCase getTopStockUseCase;
    private final AddBranchUseCase addBranchUseCase;
    private final AddProductUseCase addProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateStockUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;

    public FranchiseController(
            AddFranchiseUseCase addFranchiseUseCase,
            GetTopStockProductsByFranchiseUseCase getTopStockUseCase,
            AddBranchUseCase addBranchUseCase,
            AddProductUseCase addProductUseCase,
            DeleteProductUseCase deleteProductUseCase,
            UpdateProductStockUseCase updateStockUseCase,
            UpdateFranchiseNameUseCase updateFranchiseNameUseCase,
            UpdateBranchNameUseCase updateBranchNameUseCase,
            UpdateProductNameUseCase updateProductNameUseCase) {

        this.addFranchiseUseCase = addFranchiseUseCase;
        this.getTopStockUseCase = getTopStockUseCase;
        this.addBranchUseCase = addBranchUseCase;
        this.addProductUseCase = addProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.updateStockUseCase = updateStockUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
    }

    @Operation(
        summary = "Crear nueva franquicia",
        description = "Crea una nueva franquicia en el sistema. El nombre debe ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Franquicia creada exitosamente",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos o franquicia duplicada",
            content = @Content()
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(
            @Parameter(description = "Datos de la franquicia a crear", required = true)
            @Valid @RequestBody AddFranchiseCommand command) {
        return addFranchiseUseCase.execute(command);
    }

    @Operation(summary = "Agregar sucursal a franquicia")

   @PostMapping("/{franchiseId}/branches")
       @ResponseStatus(HttpStatus.CREATED)
       public Mono<BranchResponse> addBranch(
               @PathVariable String franchiseId,
               @Valid @RequestBody AddBranchCommand command) {
           AddBranchCommand commandWithFranchiseId = new AddBranchCommand(
                   command.name(),
                   franchiseId
           );
           return addBranchUseCase.execute(commandWithFranchiseId);
       }


    @Operation(summary = "Agregar producto a sucursal")
    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProduct(
            @PathVariable String branchId,
            @Valid @RequestBody AddProductCommand command) {
        return addProductUseCase.execute(branchId, command);
    }


   @Operation(summary = "Eliminar producto")
    @DeleteMapping("/products/{productId}")
    public Mono<Map<String, String>> deleteProduct(@PathVariable String productId) {
        return deleteProductUseCase.execute(productId)
                .thenReturn(Map.of("message", "Producto eliminado exitosamente"));
    }

    @Operation(summary = "Actualizar stock de producto")
    @PatchMapping("/products/{productId}/stock")
    public Mono<ProductResponse> updateProductStock(
            @PathVariable String productId,
            @Valid @RequestBody UpdateStockCommand command) {
        return updateStockUseCase.execute(productId, command);
    }

    @Operation(
        summary = "Obtener productos con mayor stock por sucursal",
        description = "Retorna el producto con mayor stock de cada sucursal de la franquicia especificada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de productos con mayor stock encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = TopStockProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Franquicia no encontrada",
            content = @Content()
        )
    })
    @GetMapping("/{franchiseId}/top-stock-products")
    public Flux<TopStockProductResponse> getTopStockProducts(
            @Parameter(description = "ID de la franquicia", required = true, example = "franchise-123")
            @PathVariable String franchiseId) {
        return getTopStockUseCase.execute(franchiseId);
    }

    @Operation(summary = "Actualizar nombre de franquicia")
    @PatchMapping("/{franchiseId}/name")
    public Mono<FranchiseResponse> updateFranchiseName(
            @PathVariable String franchiseId,
            @Valid @RequestBody UpdateNameCommand command) {
        return updateFranchiseNameUseCase.execute(franchiseId, command);
    }

    @Operation(summary = "Actualizar nombre de sucursal")
    @PatchMapping("/branches/{branchId}/name")
    public Mono<BranchResponse> updateBranchName(
            @PathVariable String branchId,
            @Valid @RequestBody UpdateNameCommand command) {
        return updateBranchNameUseCase.execute(branchId, command);
    }

    @Operation(summary = "Actualizar nombre de producto")
    @PatchMapping("/products/{productId}/name")
    public Mono<ProductResponse> updateProductName(
            @PathVariable String productId,
            @Valid @RequestBody UpdateNameCommand command) {
        return updateProductNameUseCase.execute(productId, command);
    }
}