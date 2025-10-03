package com.franchise.api.domain.port;

     import com.franchise.api.domain.entity.Branch;
     import com.franchise.api.domain.vo.BranchName;
     import reactor.core.publisher.Flux;
     import reactor.core.publisher.Mono;

     public interface BranchRepository {

         Mono<Branch> save(Branch branch);

         Mono<Branch> findById(String id);

         Flux<Branch> findAll();

         Mono<Void> deleteById(String id);

         Mono<Branch> findByName(BranchName name);

         Flux<Branch> findByFranchiseId(String franchiseId);
     }