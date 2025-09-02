package com.example.samplestatus;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DetailsTableRepository extends JpaRepository<DetailsTable, Integer>,
        JpaSpecificationExecutor<DetailsTable> {

    // For today's data pagination
    Page<DetailsTable> findByCreateDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    // For filtering with MRN + SampleNo + Date range
    Page<DetailsTable> findByMrnContainingIgnoreCaseAndMSampleNoContainingIgnoreCaseAndCreateDateBetween(
            String mrn,
            String mSampleNo,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
    