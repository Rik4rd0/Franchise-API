package com.franchise.api.infrastructure.adapter.document;

    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;

    import java.util.List;

    @Document(collection = "franchises")
    public class FranchiseDocument {

        @Id
        private String id;
        private String name;
        private List<BranchDocument> branches;

        public FranchiseDocument() {}

        public FranchiseDocument(String id, String name, List<BranchDocument> branches) {
            this.id = id;
            this.name = name;
            this.branches = branches;
        }

        // Getters y setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<BranchDocument> getBranches() { return branches; }
        public void setBranches(List<BranchDocument> branches) { this.branches = branches; }
    }