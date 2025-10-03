package com.franchise.api.infrastructure.adapter.controller;

                          import com.franchise.api.domain.port.FranchiseRepository;
                          import com.franchise.api.domain.port.BranchRepository;
                          import com.franchise.api.domain.port.ProductRepository;
                          import org.springframework.web.bind.annotation.GetMapping;
                          import org.springframework.web.bind.annotation.RequestMapping;
                          import org.springframework.web.bind.annotation.RestController;
                          import reactor.core.publisher.Flux;
                          import reactor.core.publisher.Mono;

                          import java.util.Map;

                          @RestController
                          @RequestMapping("/api/debug")
                          public class DebugController {

                              private final FranchiseRepository franchiseRepository;
                              private final BranchRepository branchRepository;
                              private final ProductRepository productRepository;

                              public DebugController(FranchiseRepository franchiseRepository,
                                                    BranchRepository branchRepository,
                                                    ProductRepository productRepository) {
                                  this.franchiseRepository = franchiseRepository;
                                  this.branchRepository = branchRepository;
                                  this.productRepository = productRepository;
                              }

                              @GetMapping("/count")
                              public Mono<Map<String, Long>> getCounts() {
                                  return Mono.zip(
                                      franchiseRepository.findAll().count(),
                                      branchRepository.findAll().count(),
                                      productRepository.findAll().count()
                                  ).map(tuple -> Map.of(
                                      "franchises", tuple.getT1(),
                                      "branches", tuple.getT2(),
                                      "products", tuple.getT3()
                                  ));
                              }

                              @GetMapping("/franchises")
                              public Flux<Object> getAllFranchises() {
                                  return franchiseRepository.findAll()
                                      .map(franchise -> Map.of(
                                          "id", franchise.id(),
                                          "name", franchise.name().value()
                                      ));
                              }

                              @GetMapping("/branches")
                              public Flux<Object> getAllBranches() {
                                  return branchRepository.findAll()
                                      .map(branch -> Map.of(
                                          "id", branch.id(),
                                          "name", branch.name().value(),
                                          "franchiseId", branch.franchiseId()
                                      ));
                              }

                              @GetMapping("/products")
                              public Flux<Object> getAllProducts() {
                                  return productRepository.findAll()
                                      .map(product -> Map.of(
                                          "id", product.id(),
                                          "name", product.name().value(),
                                          "stock", product.stock().value(),
                                          "branchId", product.branchId()
                                      ));
                              }
                          }