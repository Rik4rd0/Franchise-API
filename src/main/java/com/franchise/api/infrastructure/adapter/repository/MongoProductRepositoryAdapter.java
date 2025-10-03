package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.domain.entity.Product;
import com.franchise.api.domain.port.ProductRepository;
import com.franchise.api.domain.vo.ProductName;
import com.franchise.api.infrastructure.mapper.ProductMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("mongoProductRepository")
public class MongoProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository springDataRepository;
    private final ProductMapper productMapper;

    public MongoProductRepositoryAdapter(SpringDataProductRepository springDataRepository,
                                        ProductMapper productMapper) {
        this.springDataRepository = springDataRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        var document = productMapper.toDocument(product);
        return springDataRepository.save(document)
                .map(productMapper::toEntity);
    }

    @Override
    public Mono<Product> findById(String id) {
        return springDataRepository.findById(id)
                .map(productMapper::toEntity);
    }

    @Override
    public Flux<Product> findAll() {
        return springDataRepository.findAll()
                .map(productMapper::toEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return springDataRepository.deleteById(id);
    }

    @Override
    public Mono<Product> findByName(ProductName name) {
        return springDataRepository.findByName(name.value())
                .map(productMapper::toEntity);
    }

    @Override
    public Flux<Product> findByBranchId(String branchId) {
        return springDataRepository.findByBranchId(branchId)
                .map(productMapper::toEntity);
    }
}