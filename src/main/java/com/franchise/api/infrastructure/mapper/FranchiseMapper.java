package com.franchise.api.infrastructure.mapper;

                            import com.franchise.api.domain.entity.Franchise;
                            import com.franchise.api.domain.vo.FranchiseName;
                            import com.franchise.api.infrastructure.adapter.document.FranchiseDocument;
                            import org.springframework.stereotype.Component;

                            @Component
                            public class FranchiseMapper {

                                private final BranchMapper branchMapper;

                                public FranchiseMapper(BranchMapper branchMapper) {
                                    this.branchMapper = branchMapper;
                                }

                                public FranchiseDocument toDocument(Franchise franchise) {
                                    var branchDocuments = franchise.branches().stream()
                                            .map(branchMapper::toDocument)
                                            .toList();

                                    return new FranchiseDocument(
                                            franchise.id(),
                                            franchise.name().value(),
                                            branchDocuments
                                    );
                                }

                                public Franchise toEntity(FranchiseDocument document) {
                                    var branches = document.getBranches().stream()
                                            .map(branchMapper::toEntity)
                                            .toList();

                                    return new Franchise(
                                            document.getId(),
                                            new FranchiseName(document.getName()),
                                            branches
                                    );
                                }
                            }