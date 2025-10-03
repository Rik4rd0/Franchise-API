package com.franchise.api.infrastructure.adapter.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "branches")
public class BranchDocument {

    @Id
    private String id;
    private String name;
    private String franchiseId;
    private List<ProductDocument> products;

    public BranchDocument() {}

    public BranchDocument(String id, String name, String franchiseId, List<ProductDocument> products) {
        this.id = id;
        this.name = name;
        this.franchiseId = franchiseId;
        this.products = products;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFranchiseId() { return franchiseId; }
    public void setFranchiseId(String franchiseId) { this.franchiseId = franchiseId; }

    public List<ProductDocument> getProducts() { return products; }
    public void setProducts(List<ProductDocument> products) { this.products = products; }
}