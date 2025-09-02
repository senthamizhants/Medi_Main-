package com.example.addmachine;

import com.example.machine.Machine;
import com.example.serialport.SerialPortConfig;
import com.example.serialport.SerialPortRepository;

import java.util.List;

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


    @GetMapping("/add-machine")
    public String showForm(Model model) {
        model.addAttribute("machine", new SerialPortConfig()); // ✅ use SerialPortConfig
        return "AddMachine";
    }
    // rs232 save configuration working
    @PostMapping("/add-machine")
    public String saveMachine(@ModelAttribute("machine") SerialPortConfig config) {
        // save using your repo
    	rs232Repository.save(config);
        return "redirect:/home";
    }
    @PostMapping("/api/saveRs232Config")
    @ResponseBody
    public SerialPortConfig saveRs232Config(@ModelAttribute SerialPortConfig rs232) {
        return rs232Repository.save(rs232); // returns saved machine as JSON
    }
    @GetMapping("/api/machines")
    @ResponseBody
    public List<SerialPortConfig> getMachines() {
        return rs232Repository.findByIsActive(1);
    }
    
  /*  @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute AddMachine machine) {
        machineService.saveMachineWithConfig(machine);
        return "redirect:/home?success";
    } */
}

