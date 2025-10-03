package com.franchise.api.infrastructure.mapper;

                    import com.franchise.api.domain.entity.Branch;
                    import com.franchise.api.domain.vo.BranchName;
                    import com.franchise.api.infrastructure.adapter.document.BranchDocument;
                    import org.springframework.stereotype.Component;

                    @Component
                    public class BranchMapper {

                        private final ProductMapper productMapper;

                        public BranchMapper(ProductMapper productMapper) {
                            this.productMapper = productMapper;
                        }

                        public BranchDocument toDocument(Branch branch) {
                            var productDocuments = branch.products().stream()
                                    .map(productMapper::toDocument)
                                    .toList();

                            return new BranchDocument(
                                    branch.id(),
                                    branch.name().value(),
                                    branch.franchiseId(),
                                    productDocuments
                            );
                        }

                        public Branch toEntity(BranchDocument document) {
                            var products = document.getProducts().stream()
                                    .map(productMapper::toEntity)
                                    .toList();

                            return new Branch(
                                    document.getId(),
                                    new BranchName(document.getName()),
                                    document.getFranchiseId(),
                                    products
                            );
                        }
                    }