package com.example.serialport;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SerialPortRepository extends JpaRepository<SerialPortConfig, Integer> {
	
	 List<SerialPortConfig> findByIsActive(int isActive);
}
