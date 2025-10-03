package com.franchise.api.application.usecase;

        import com.franchise.api.domain.entity.Product;
        import com.franchise.api.domain.vo.FranchiseName;
        import com.franchise.api.domain.vo.Stock;

        public record ProductResponse (
                String id,
                String name,
                Stock stock
                ){

            public static ProductResponse fromDomain(Product product) {
                return new ProductResponse(
                        product.id(),
                        product.name().value(),
                        product.stock()
                );
            }
        }