package com.franchise.api.application.service;

                                     import com.franchise.api.application.usecase.TopStockProductResponse;
                                     import com.franchise.api.domain.port.FranchiseRepository;
                                     import com.franchise.api.domain.port.BranchRepository;
                                     import com.franchise.api.domain.port.ProductRepository;
                                     import com.franchise.api.domain.exception.FranchiseNotFoundException;
                                     import org.springframework.stereotype.Service;
                                     import reactor.core.publisher.Flux;
                                     import reactor.core.publisher.Mono;

                                     @Service
                                     public class GetTopStockProductsByFranchiseUseCase {

                                         private final FranchiseRepository franchiseRepository;
                                         private final BranchRepository branchRepository;
                                         private final ProductRepository productRepository;

                                         public GetTopStockProductsByFranchiseUseCase(
                                                 FranchiseRepository franchiseRepository,
                                                 BranchRepository branchRepository,
                                                 ProductRepository productRepository) {
                                             this.franchiseRepository = franchiseRepository;
                                             this.branchRepository = branchRepository;
                                             this.productRepository = productRepository;
                                         }

                                         public Flux<TopStockProductResponse> execute(String franchiseId) {
                                             return franchiseRepository.findById(franchiseId)
                                                 .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada: " + franchiseId)))
                                                 .flatMapMany(franchise ->
                                                     branchRepository.findByFranchiseId(franchiseId)
                                                         .flatMap(branch ->
                                                             productRepository.findByBranchId(branch.id())
                                                                 .collectList()
                                                                 .filter(products -> !products.isEmpty())
                                                                 .map(products -> products.stream()
                                                                     .max((p1, p2) -> Integer.compare(p1.stock().value(), p2.stock().value()))
                                                                     .orElse(null))
                                                                 .filter(product -> product != null)
                                                                 .map(product -> TopStockProductResponse.fromDomain(product, branch.name().value()))
                                                         )
                                                 );
                                         }
                                     }