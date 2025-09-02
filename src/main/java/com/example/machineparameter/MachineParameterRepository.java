package com.example.machineparameter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineParameterRepository extends JpaRepository<MachineParameter, Long> {

    boolean existsByTestId(String testId);

    Page<MachineParameter> findAll(Pageable pageable);

    @Query("SELECT m FROM MachineParameter m WHERE " +
           "LOWER(m.testId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.testName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.machineName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.locationName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MachineParameter> search(@Param("keyword") String keyword, Pageable pageable);
    
    Page<MachineParameter> findByTestIdContainingIgnoreCaseOrTestNameContainingIgnoreCase(
    	    String testId, String testName, Pageable pageable);
    
    MachineParameter findByTestId(String testId);
    List<MachineParameter> findByMachineId(Long machineId);
    
    @Query("SELECT DISTINCT new com.example.machineparameter.MachineParameter(m.machineId, m.machineName) FROM MachineParameter m")
    List<MachineParameter> getDistinctMachines();
    
    @Query("SELECT DISTINCT new com.example.machineparameter.MachineParameter(m.machineId, m.machineName) FROM MachineParameter m WHERE m.machineId IS NOT NULL")
    List<MachineParameter> findDistinctByMachineIdAndMachineName();
}