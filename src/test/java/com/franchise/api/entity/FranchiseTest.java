package com.franchise.api.entity;

import com.franchise.api.domain.entity.Branch;
import com.franchise.api.domain.entity.Franchise;
import com.franchise.api.domain.entity.Product;
import com.franchise.api.domain.exception.BusinessRuleException;
import com.franchise.api.domain.vo.BranchName;
import com.franchise.api.domain.vo.FranchiseName;
import com.franchise.api.domain.vo.ProductName;
import com.franchise.api.domain.vo.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la entidad Franchise")
class FranchiseTest {

    @Test
    @DisplayName("Debe crear una nueva franquicia correctamente")
    void shouldCreateNewFranchise() {
        // Given
        String franchiseName = "McDonald's";

        // When
        Franchise franchise = Franchise.createNew(franchiseName);

        // Then
        assertNotNull(franchise.id());
        assertEquals(franchiseName, franchise.name().value());
        assertTrue(franchise.branches().isEmpty());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre es nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> new Franchise("id", null, List.of()));
    }

    @Test
    @DisplayName("Debe agregar una sucursal correctamente")
    void shouldAddBranchSuccessfully() {
        // Given
        Franchise franchise = Franchise.createNew("KFC");
        Branch branch = Branch.createNew("Sucursal Centro", franchise.id());

        // When
        Franchise updatedFranchise = franchise.addBranch(branch);

        // Then
        assertEquals(1, updatedFranchise.branches().size());
        assertEquals("Sucursal Centro", updatedFranchise.branches().get(0).name().value());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la sucursal no pertenece a la franquicia")
    void shouldThrowExceptionWhenBranchDoesNotBelongToFranchise() {
        // Given
        Franchise franchise = Franchise.createNew("Burger King");
        Branch branch = Branch.createNew("Sucursal Centro", "otro-franchise-id");

        // When & Then
        assertThrows(BusinessRuleException.class, 
            () -> franchise.addBranch(branch));
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la franquicia")
    void shouldUpdateFranchiseName() {
        // Given
        Franchise franchise = Franchise.createNew("Pizza Hut");
        String newName = "Pizza Hut Express";

        // When
        Franchise updatedFranchise = franchise.updateName(newName);

        // Then
        assertEquals(newName, updatedFranchise.name().value());
        assertEquals(franchise.id(), updatedFranchise.id());
    }

    @Test
    @DisplayName("Debe encontrar el producto con más stock por sucursal")
    void shouldFindMaxStockedProductPerBranch() {
        // Given
        Franchise franchise = Franchise.createNew("Subway");
        Branch branch1 = Branch.createNew("Sucursal Norte", franchise.id());
        Product product1 = Product.createNew("Sandwich", 10, branch1.id());
        Product product2 = Product.createNew("Bebida", 5, branch1.id());
        
        Branch branchWithProducts = branch1.addProduct(product1).addProduct(product2);
        Franchise franchiseWithBranch = franchise.addBranch(branchWithProducts);

        // When
        List<Product> maxStockedProducts = franchiseWithBranch.findMaxStockedProductPerBranch();

        // Then
        assertEquals(1, maxStockedProducts.size());
        assertEquals("Sandwich", maxStockedProducts.get(0).name().value());
        assertEquals(10, maxStockedProducts.get(0).stock().value());
    }

    @Test
    @DisplayName("Debe reemplazar una sucursal existente")
    void shouldReplaceBranch() {
        // Given
        Franchise franchise = Franchise.createNew("Domino's");
        Branch originalBranch = Branch.createNew("Sucursal Original", franchise.id());
        Franchise franchiseWithBranch = franchise.addBranch(originalBranch);
        
        Branch updatedBranch = originalBranch.updateName("Sucursal Actualizada");

        // When
        Franchise updatedFranchise = franchiseWithBranch.replaceBranch(updatedBranch);

        // Then
        assertEquals(1, updatedFranchise.branches().size());
        assertEquals("Sucursal Actualizada", 
            updatedFranchise.branches().get(0).name().value());
    }

    @Test
    @DisplayName("Debe lanzar excepción al reemplazar sucursal inexistente")
    void shouldThrowExceptionWhenReplacingNonExistentBranch() {
        // Given
        Franchise franchise = Franchise.createNew("Taco Bell");
        Branch nonExistentBranch = Branch.createNew("Sucursal Falsa", franchise.id());

        // When & Then
        assertThrows(BusinessRuleException.class, 
            () -> franchise.replaceBranch(nonExistentBranch));
    }

    @Test
    @DisplayName("Debe eliminar una sucursal correctamente")
    void shouldRemoveBranch() {
        // Given
        Franchise franchise = Franchise.createNew("Starbucks");
        Branch branch1 = Branch.createNew("Sucursal 1", franchise.id());
        Branch branch2 = Branch.createNew("Sucursal 2", franchise.id());
        
        Franchise franchiseWithBranches = franchise
            .addBranch(branch1)
            .addBranch(branch2);

        // When
        Franchise updatedFranchise = franchiseWithBranches.removeBranch(branch1.id());

        // Then
        assertEquals(1, updatedFranchise.branches().size());
        assertEquals("Sucursal 2", updatedFranchise.branches().get(0).name().value());
    }
}
