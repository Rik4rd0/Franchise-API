package com.franchise.api.domain.entity;

                    import com.franchise.api.domain.vo.ProductName;
                    import com.franchise.api.domain.vo.Stock;

                    import java.util.Objects;
                    import java.util.UUID;

                    /**
                     * Entidad de Dominio: Producto
                     */
                    public record Product(
                            String id,
                            ProductName name,
                            Stock stock,
                            String branchId
                    ) {
                        public Product {
                            Objects.requireNonNull(name, "El nombre del producto no puede ser nulo.");
                            Objects.requireNonNull(stock, "El stock del producto no puede ser nulo.");
                            Objects.requireNonNull(branchId, "El ID de la sucursal no puede ser nulo.");
                        }

                        /**
                         * Crea un nuevo producto.
                         */
                        public static Product createNew(String name, int stock, String branchId) {
                            return new Product(
                                    UUID.randomUUID().toString(),
                                    new ProductName(name),
                                    new Stock(stock),
                                    branchId
                            );
                        }

                        /**
                         * Asigna un ID al producto (útil después de persistir).
                         */
                        public Product withId(String id) {
                            return new Product(id, name, stock, branchId);
                        }

                        /**
                         * Actualiza el stock del producto.
                         */
                        public Product updateStock(int newStock) {
                            return new Product(
                                    this.id,
                                    this.name,
                                    new Stock(newStock),
                                    this.branchId
                            );
                        }

                        /**
                         * Actualiza el nombre del producto.
                         */
                        public Product updateName(String newName) {
                            return new Product(
                                    this.id,
                                    new ProductName(newName),
                                    this.stock,
                                    this.branchId
                            );
                        }


                    }