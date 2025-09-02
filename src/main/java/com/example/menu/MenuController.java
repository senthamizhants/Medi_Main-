package com.example.menu;

import com.example.serialport.SerialPortConfig;
import com.example.serialport.SerialPortRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MenuController {

    private final SerialPortRepository serialRepo;

    public MenuController(SerialPortRepository serialRepo) {
        this.serialRepo = serialRepo;
    }

    // Makes the machine list available for all Thymeleaf pages
    @ModelAttribute("machines")
    public List<SerialPortConfig> getMachines() {
        return serialRepo.findByIsActive(1);
    }
}
