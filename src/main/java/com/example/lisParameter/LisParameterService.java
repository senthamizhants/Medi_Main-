package com.example.lisParameter;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LisParameterService {

    @Autowired
    private LisParameterRepository repository;

    public void save(LisParameter entity) {
        repository.save(entity);
    }

    public void update(LisParameter entity) {
        repository.save(entity); // You can enhance this to check and merge if needed
    }

    public Page<LisParameter> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<LisParameter> search(String keyword, Pageable pageable) {
        return repository.findByParameterIdContainingIgnoreCaseOrParameterNameContainingIgnoreCase(keyword, keyword, pageable);
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public List<LisParameter> getAll() {
        return repository.findAll();
    }
  //  public List<LisParameter> getAllParameters() {
 //       return repository.findAll();
 //   }
    
    public List<LisParameter> getAllParameters() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public LisParameter getByParameterId(String parameterId) {
        return repository.findByParameterId(parameterId);
    }
    public boolean existsByParameterId(String parameterId) {
        return repository.existsByParameterIdIgnoreCase(parameterId.trim());
    }
    
}