package com.example.mappingscreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lisParameter.LisParameter;
import com.example.lisParameter.LisParameterRepository;
import com.example.location.Location;
import com.example.location.LocationRepository;
import com.example.machine.Machine;
import com.example.machine.MachineRepository;
import com.example.machineparameter.MachineParameter;
import com.example.machineparameter.MachineParameterRepository;

import java.util.List;

@Service
public class MappingScreenService {
	 @Autowired private LisParameterRepository lisRepo;
	    @Autowired private MachineParameterRepository machineParamRepo;
	    @Autowired private MachineRepository machineRepo;
	    @Autowired private LocationRepository locationRepo;

	    public List<LisParameter> getAllLisParameters() {
	        return lisRepo.findAll();
	    }

	    public List<MachineParameter> getAllMachineParameters() {
	        return machineParamRepo.findAll();
	    }

	    public List<Machine> getAllMachines() {
	        return machineRepo.findAll();
	    }

	    public List<Location> getAllLocations() {
	        return locationRepo.findAll();
	    }
	}
	
