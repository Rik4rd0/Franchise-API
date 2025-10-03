package com.franchise.api.infrastructure.adapter.repository;

import com.franchise.api.domain.entity.Branch;
import com.franchise.api.domain.port.BranchRepository;
import com.franchise.api.domain.vo.BranchName;
import com.franchise.api.infrastructure.mapper.BranchMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("mongoBranchRepository")
public class MongoBranchRepositoryAdapter implements BranchRepository {

    private final SpringDataBranchRepository springDataRepository;
    private final BranchMapper branchMapper;

    public MongoBranchRepositoryAdapter(SpringDataBranchRepository springDataRepository,
                                       BranchMapper branchMapper) {
        this.springDataRepository = springDataRepository;
        this.branchMapper = branchMapper;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        var document = branchMapper.toDocument(branch);
        return springDataRepository.save(document)
                .map(branchMapper::toEntity);
    }

    @Override
    public Mono<Branch> findById(String id) {
        return springDataRepository.findById(id)
                .map(branchMapper::toEntity);
    }

    @Override
    public Flux<Branch> findAll() {
        return springDataRepository.findAll()
                .map(branchMapper::toEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return springDataRepository.deleteById(id);
    }

    @Override
    public Mono<Branch> findByName(BranchName name) {
        return springDataRepository.findByName(name.value())
                .map(branchMapper::toEntity);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return springDataRepository.findByFranchiseId(franchiseId)
                .map(branchMapper::toEntity);
    }
}