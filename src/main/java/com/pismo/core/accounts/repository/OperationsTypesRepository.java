package com.pismo.core.accounts.repository;

import com.pismo.core.accounts.domain.OperationsTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OperationsTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationsTypesRepository extends JpaRepository<OperationsTypes, Long> {}
