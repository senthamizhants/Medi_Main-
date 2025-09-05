package com.example.mappingscreen;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MappingScreenRepository extends JpaRepository<MappingScreen, Long> {
	
	  boolean existsByTestIdAndParameterId(String testId, String parameterId);
	    List<MappingScreen> findByTestId(String testId);

	
	
	
	
	
}



