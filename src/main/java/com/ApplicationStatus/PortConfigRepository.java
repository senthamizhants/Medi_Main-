package com.ApplicationStatus;

import com.ApplicationStatus.PortConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortConfigRepository extends JpaRepository<PortConfig, Integer> {
    List<PortConfig> findByLocationId(int locationId);
}
