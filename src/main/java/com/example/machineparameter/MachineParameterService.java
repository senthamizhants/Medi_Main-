package com.example.machineparameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.location.Location;
import com.example.location.LocationService;
import com.example.machine.Machine;
import com.example.machine.MachineService;

import java.util.List;

@Service
public class MachineParameterService {

    @Autowired
    private MachineParameterRepository machineParameterRepository;
    
    @Autowired
    private LocationService locationService;

    @Autowired
    private MachineService machineService;

    
    public String save(MachineParameter param) {
        // Check for duplicate TestId
        if (machineParameterRepository.existsByTestId(param.getTestId())) {
            return "Duplicate Test ID.";
        }

     // 🟡 Set Location Name
        Location location = locationService.getLocationById(param.getLocationId());
        if (location != null) {
            param.setLocationName(location.getLocation());
        }

        // 🟡 Set Machine Name
        Machine machine = machineService.getMachineById(param.getMachineId());
        if (machine != null) {
            param.setMachineName(machine.getMachineName());
        }
        
        // Save only if no duplicate
        machineParameterRepository.save(param);
        return "Saved successfully.";
    }

    // ✅ Add this method to get all records for the grid/table
    public List<MachineParameter> getAll() {
        return machineParameterRepository.findAll();
    }
    
    public void deleteById(Long id) {
        machineParameterRepository.deleteById(id);
    }
    public void update(MachineParameter param) {
        if (param.getId() != null && machineParameterRepository.existsById(param.getId())) {

            // 🟡 Set Location and Machine Name on update too
            Location location = locationService.getLocationById(param.getLocationId());
            if (location != null) {
                param.setLocationName(location.getLocation());
            }

            Machine machine = machineService.getMachineById(param.getMachineId());
            if (machine != null) {
                param.setMachineName(machine.getMachineName());
            }

            machineParameterRepository.save(param);  // JPA updates if ID exists
        }
    }
    public Page<MachineParameter> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return machineParameterRepository.findAll(pageable);
    }
    public Page<MachineParameter> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return machineParameterRepository.search(keyword, pageable);
    }
    public Page<MachineParameter> searchMachineParameters(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return machineParameterRepository.findByTestIdContainingIgnoreCaseOrTestNameContainingIgnoreCase(
            keyword, keyword, pageable);
    }
    public MachineParameter getByTestId(String testId) {
        return machineParameterRepository.findByTestId(testId);
    }
    public List<MachineParameter> getTestsByMachineId(Long machineId) {
        return machineParameterRepository.findByMachineId(machineId);
    }
    
    public List<MachineParameter> getDistinctMachines() {
        return machineParameterRepository.findDistinctByMachineIdAndMachineName();
    }
    
   
    
    
    
    public List<MachineParameter> getMachinesByLocation(Long locationId) {
        return machineParameterRepository.findMachinesByLocation(locationId);
    }

    public List<MachineParameter> getTestsByMachineAndLocation(Long machineId, Long locationId) {
        return machineParameterRepository.findByMachineIdAndLocationId(machineId, locationId);
    }

}