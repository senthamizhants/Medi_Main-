package com.example.lablink.repo;

import com.example.lablink.domain.ConnectionConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionConfigRepository extends JpaRepository<ConnectionConfig, Long> {
}
