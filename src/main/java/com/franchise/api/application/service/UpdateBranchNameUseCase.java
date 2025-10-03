package com.franchise.api.application.service;

        import com.franchise.api.application.usecase.UpdateNameCommand;
        import com.franchise.api.application.usecase.BranchResponse;
        import com.franchise.api.domain.entity.Branch;
        import com.franchise.api.domain.port.BranchRepository;
        import com.franchise.api.domain.exception.BranchNotFoundException;
        import org.springframework.stereotype.Service;
        import reactor.core.publisher.Mono;

        @Service
        public class UpdateBranchNameUseCase {

            private final BranchRepository branchRepository;

            public UpdateBranchNameUseCase(BranchRepository branchRepository) {
                this.branchRepository = branchRepository;
            }

           public Mono<BranchResponse> execute(String branchId, UpdateNameCommand command) {
                return branchRepository.findById(branchId)
                    .switchIfEmpty(Mono.error(new BranchNotFoundException("Sucursal no encontrada: " + branchId)))
                    .flatMap(branch -> {
                        Branch updatedBranch = branch.updateName(command.name());
                        return branchRepository.save(updatedBranch);
                    })
                    .map(BranchResponse::fromDomain);
            }
        }