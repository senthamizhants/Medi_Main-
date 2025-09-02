package com.example.lisParameter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LisParameterRepository extends JpaRepository<LisParameter, Long> {
    Page<LisParameter> findByParameterIdContainingIgnoreCaseOrParameterNameContainingIgnoreCase(
        String parameterId, String parameterName, Pageable pageable);
    LisParameter findByParameterId(String parameterId);
    boolean existsByParameterIdIgnoreCase(String parameterId);
}