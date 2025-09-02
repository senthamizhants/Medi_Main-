package com.example.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }
    
    public Machine getMachineById(Long id) {
        return machineRepository.findById(id).orElse(null);
    }
    public String getMachineNameById(Long id) {
        return machineRepository.findById(id)
                .map(Machine::getMachineName)
                .orElse("Unknown");
    }
}