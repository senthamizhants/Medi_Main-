package com.example.datatracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DataTrackerRepository extends JpaRepository<DataTrackerEntity, Long> {

    @Query("SELECT DISTINCT m.machineName FROM DataTrackerEntity m")
    List<String> findDistinctMachineNames();

    @Query("SELECT DISTINCT m.protocol FROM DataTrackerEntity m WHERE m.machineName = :machineName")
    List<String> findProtocolsByMachine(@Param("machineName") String machineName);

    @Query("SELECT m FROM DataTrackerEntity m " +
            "WHERE (:machineName IS NULL OR m.machineName = :machineName) " +
            "AND (:protocol IS NULL OR m.protocol = :protocol) " +
            "AND (:fromDate IS NULL OR m.receivedDate >= :fromDate) " +
            "AND (:toDate IS NULL OR m.receivedDate <= :toDate)")
    List<DataTrackerEntity> searchRawData(@Param("machineName") String machineName,
                                          @Param("protocol") String protocol,
                                          @Param("fromDate") LocalDateTime fromDate,
                                          @Param("toDate") LocalDateTime toDate);

    @Query("SELECT d FROM DataTrackerEntity d " +
            "WHERE (:machineName IS NULL OR d.machineName = :machineName) " +
            "AND (:protocol IS NULL OR d.protocol = :protocol) " +
            "AND (:fromDate IS NULL OR d.receivedDate >= :fromDate) " +
            "AND (:toDate IS NULL OR d.receivedDate <= :toDate) " +
            "AND (:sampleNumber IS NULL OR LOWER(d.sampleNumber) LIKE LOWER(CONCAT('%', :sampleNumber, '%')))")
    List<DataTrackerEntity> searchByFilters(@Param("machineName") String machineName,
                                            @Param("protocol") String protocol,
                                            @Param("fromDate") LocalDateTime fromDate,
                                            @Param("toDate") LocalDateTime toDate,
                                            @Param("sampleNumber") String sampleNumber);
}
