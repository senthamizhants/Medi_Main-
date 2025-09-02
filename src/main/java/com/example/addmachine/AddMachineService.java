package com.example.addmachine;

import com.example.serialport.SerialPortConfig;

import com.example.serialport.SerialPortRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AddMachineService {
	 private final SerialPortRepository rs232Repository;

	    public AddMachineService(SerialPortRepository rs232Repository) {
	        this.rs232Repository = rs232Repository;
	    }

	    // Save AddMachine + RS232 fields in same table
	    public SerialPortConfig saveMachineWithConfig(SerialPortConfig config) {
	       return rs232Repository.save(config);
	    }
	    public List<SerialPortConfig> getAllMachines() {

	        return rs232Repository.findAll();

	    }

	   
	}