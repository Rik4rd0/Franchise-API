package com.franchise.api.domain.vo;

            import com.franchise.api.domain.exception.DomainValidationException;

            public record Stock(int value) {
                public Stock {
                    if (value < 0) {
                        throw new DomainValidationException("El stock no puede ser negativo.");
                    }
                }

                @Override
                public String toString() {
                    return String.valueOf(value);
                }
            }