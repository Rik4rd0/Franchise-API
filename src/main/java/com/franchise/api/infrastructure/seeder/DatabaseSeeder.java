package com.franchise.api.infrastructure.seeder;

                            import com.franchise.api.domain.entity.Branch;
                            import com.franchise.api.domain.entity.Franchise;
                            import com.franchise.api.domain.entity.Product;
                            import com.franchise.api.domain.port.BranchRepository;
                            import com.franchise.api.domain.port.FranchiseRepository;
                            import com.franchise.api.domain.port.ProductRepository;
                            import lombok.extern.slf4j.Slf4j;
                            import org.springframework.boot.ApplicationArguments;
                            import org.springframework.boot.ApplicationRunner;
                            import org.springframework.stereotype.Component;
                            import reactor.core.publisher.Mono;

                            @Component
                            @Slf4j
                            public class DatabaseSeeder implements ApplicationRunner {

                                private final FranchiseRepository franchiseRepository;
                                private final BranchRepository branchRepository;
                                private final ProductRepository productRepository;

                                public DatabaseSeeder(FranchiseRepository franchiseRepository,
                                                     BranchRepository branchRepository,
                                                     ProductRepository productRepository) {
                                    this.franchiseRepository = franchiseRepository;
                                    this.branchRepository = branchRepository;
                                    this.productRepository = productRepository;
                                }

                                @Override
                                public void run(ApplicationArguments args) {
                                    log.info("Iniciando seeding de la base de datos...");
                                    seedDatabase()
                                        .doOnSuccess(v -> log.info("Seeding completado exitosamente"))
                                        .doOnError(error -> log.error("Error durante el seeding: {}", error.getMessage(), error))
                                        .block(); // Importante: bloquear para esperar completar el seeding
                                }

                                private Mono<Void> seedDatabase() {
                                    return franchiseRepository.findAll()
                                        .hasElements()
                                        .flatMap(exists -> {
                                            if (!exists) {
                                                log.info("No existen datos, iniciando seeding inicial");
                                                return seedInitialData();
                                            } else {
                                                log.info("Los datos ya existen, saltando seeding");
                                                return Mono.empty();
                                            }
                                        })
                                        .onErrorResume(error -> {
                                            log.error("Error en seedDatabase: ", error);
                                            return Mono.empty();
                                        });
                                }

                                private Mono<Void> seedInitialData() {
                                    log.info("Creando franquicias...");

                                    Franchise mcdonalds = Franchise.createNew("McDonald's");

                                    return franchiseRepository.save(mcdonalds)
                                        .doOnSuccess(saved -> log.info("Franquicia guardada: {} con ID: {}", saved.name().value(), saved.id()))
                                        .doOnError(error -> log.error("Error guardando franquicia: ", error))
                                        .flatMap(savedMcdonalds -> {
                                            log.info("Creando sucursales para McDonald's...");

                                            // Crear sucursales
                                            Branch mcBranch1 = Branch.createNew("McDonald's Centro", savedMcdonalds.id());
                                            Branch mcBranch2 = Branch.createNew("McDonald's Norte", savedMcdonalds.id());

                                            return branchRepository.save(mcBranch1)
                                                .doOnSuccess(saved -> log.info("Sucursal guardada: {} con ID: {}", saved.name().value(), saved.id()))
                                                .doOnError(error -> log.error("Error guardando sucursal 1: ", error))
                                                .flatMap(savedBranch1 ->
                                                    branchRepository.save(mcBranch2)
                                                        .doOnSuccess(saved -> log.info("Sucursal guardada: {} con ID: {}", saved.name().value(), saved.id()))
                                                        .doOnError(error -> log.error("Error guardando sucursal 2: ", error))
                                                        .flatMap(savedBranch2 -> createProducts(savedBranch1.id(), savedBranch2.id()))
                                                );
                                        })
                                        .then(createOtherFranchises())
                                        .doOnSuccess(v -> log.info("Seeding inicial completado"))
                                        .doOnError(error -> log.error("Error en seeding inicial: ", error))
                                        .onErrorResume(error -> {
                                            log.error("Error completo en seedInitialData: ", error);
                                            return Mono.empty();
                                        });
                                }

                                private Mono<Void> createProducts(String branch1Id, String branch2Id) {
                                    log.info("Creando productos para sucursales: {} y {}", branch1Id, branch2Id);

                                    Product bigMac = Product.createNew("Big Mac", 50, branch1Id);
                                    Product mcFlurry = Product.createNew("McFlurry", 30, branch1Id);
                                    Product quarterPounder = Product.createNew("Quarter Pounder", 25, branch2Id);

                                    return productRepository.save(bigMac)
                                        .doOnSuccess(saved -> log.info("Producto guardado: {} con stock: {}", saved.name().value(), saved.stock().value()))
                                        .doOnError(error -> log.error("Error guardando producto Big Mac: ", error))
                                        .then(productRepository.save(mcFlurry))
                                        .doOnSuccess(saved -> log.info("Producto guardado: {} con stock: {}", saved.name().value(), saved.stock().value()))
                                        .doOnError(error -> log.error("Error guardando producto McFlurry: ", error))
                                        .then(productRepository.save(quarterPounder))
                                        .doOnSuccess(saved -> log.info("Producto guardado: {} con stock: {}", saved.name().value(), saved.stock().value()))
                                        .doOnError(error -> log.error("Error guardando producto Quarter Pounder: ", error))
                                        .then()
                                        .onErrorResume(error -> {
                                            log.error("Error creando productos: ", error);
                                            return Mono.empty();
                                        });
                                }

                                private Mono<Void> createOtherFranchises() {
                                    log.info("Creando otras franquicias...");

                                    Franchise burgerKing = Franchise.createNew("Burger King");
                                    Franchise kfc = Franchise.createNew("KFC");

                                    return franchiseRepository.save(burgerKing)
                                        .doOnSuccess(saved -> log.info("Franquicia Burger King guardada con ID: {}", saved.id()))
                                        .then(franchiseRepository.save(kfc))
                                        .doOnSuccess(saved -> log.info("Franquicia KFC guardada con ID: {}", saved.id()))
                                        .then()
                                        .onErrorResume(error -> {
                                            log.error("Error creando otras franquicias: ", error);
                                            return Mono.empty();
                                        });
                                }
                            }