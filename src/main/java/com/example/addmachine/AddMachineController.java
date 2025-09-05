package com.example.addmachine;

import com.example.location.Location;
import com.example.location.LocationRepository;
import com.example.location.LocationService;
import com.example.machine.Machine;
import com.example.serialport.SerialPortConfig;
import com.example.serialport.SerialPortRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class AddMachineController {
	private final AddMachineService machineService;
    private final SerialPortRepository rs232Repository;

    public AddMachineController(AddMachineService machineService, SerialPortRepository repo) {
        this.machineService = machineService;
        this.rs232Repository = repo;
    }
    @Autowired
    private LocationService locationService;

    @GetMapping("/add-machine")
    public String showForm(Model model) {
        model.addAttribute("machine", new SerialPortConfig()); // use SerialPortConfig
        List<Location> locations = locationService.getAllLocations();
        
        model.addAttribute("locations", locations);
        return "AddMachine";
    }
    
    @Autowired
    private LocationRepository locationRepository;

  
    // rs232 save configuration working
    @PostMapping("/add-machine")
    public String saveMachine(@ModelAttribute("machine") SerialPortConfig config) {
    	
    	if (config.getLocationId() != null) {
            Long locId = config.getLocationId();  // already Long, no parsing needed
            Location loc = locationService.findById(locId);
            if (loc != null) {
                config.setLocationName(loc.getLocation());
            }
        }
        // save using your repo
    	rs232Repository.save(config);
        return "redirect:/home";
    }
      
     
  /*  @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute AddMachine machine) {
        machineService.saveMachineWithConfig(machine);
        return "redirect:/home?success";
    } */
}

