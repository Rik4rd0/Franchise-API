package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.domain.entity.Franchise;
import com.franchise.api.domain.port.FranchiseRepository;
import com.franchise.api.domain.vo.FranchiseName;
import com.franchise.api.infrastructure.mapper.FranchiseMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("mongoFranchiseRepository")
public class MongoFranchiseRepositoryAdapter implements FranchiseRepository {

    private final SpringDataFranchiseRepository springDataRepository;
    private final FranchiseMapper franchiseMapper;

    public MongoFranchiseRepositoryAdapter(SpringDataFranchiseRepository springDataRepository,
                                          FranchiseMapper franchiseMapper) {
        this.springDataRepository = springDataRepository;
        this.franchiseMapper = franchiseMapper;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        var document = franchiseMapper.toDocument(franchise);
        return springDataRepository.save(document)
                .map(franchiseMapper::toEntity);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return springDataRepository.findById(id)
                .map(franchiseMapper::toEntity);
    }

    @Override
    public Flux<Franchise> findAll() {
        return springDataRepository.findAll()
                .map(franchiseMapper::toEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return springDataRepository.deleteById(id);
    }

    @Override
    public Mono<Franchise> findByName(FranchiseName name) {
        return springDataRepository.findByName(name.value())
                .map(franchiseMapper::toEntity);
    }

    @Override
    public Mono<Franchise> findFranchiseWithBranchDetails(String franchiseId) {
        // En MongoDB con documentos embebidos, es la misma operación que findById
        return findById(franchiseId);
    }
}