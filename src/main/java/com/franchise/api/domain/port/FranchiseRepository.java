package com.franchise.api.domain.port;

     import com.franchise.api.domain.entity.Franchise;
     import com.franchise.api.domain.vo.FranchiseName;
     import reactor.core.publisher.Flux;
     import reactor.core.publisher.Mono;

     /**
      * Puerto de salida (Outbound Port) para gestionar la persistencia de la entidad Franchise.
      * Implementa operaciones reactivas CRUD y consultas de negocio específicas.
      */
     public interface FranchiseRepository {

         /**
          * Guarda o actualiza una entidad Franchise.
          */
         Mono<Franchise> save(Franchise franchise);

         /**
          * Busca una entidad Franchise por su identificador único.
          */
         Mono<Franchise> findById(String id);

         /**
          * Recupera todas las entidades Franchise.
          */
         Flux<Franchise> findAll();

         /**
          * Elimina una entidad Franchise por su identificador.
          */
         Mono<Void> deleteById(String id);

         /**
          * Busca una franquicia por nombre.
          */
         Mono<Franchise> findByName(FranchiseName name);

         /**
          * OPERACIÓN DE NEGOCIO ESPECÍFICA (CRITERIO 7).
          */
         Mono<Franchise> findFranchiseWithBranchDetails(String franchiseId);
     }