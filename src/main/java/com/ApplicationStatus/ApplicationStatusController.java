package com.ApplicationStatus;

import com.ApplicationStatus.PortConfig;
import com.ApplicationStatus.PortConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ApplicationStatusController {

    @Autowired
    private PortConfigRepository portConfigRepository;

    // Location cards page
    @GetMapping("/locations")
    public String getLocations() {
        return "AccessLocationAppStus";  // must match AccessLocationAppStus.html
    }

    // Application status page
    @GetMapping("/application-status")
    public String getApplicationStatus(@RequestParam("locationId") int locationId, Model model) {
        List<PortConfig> machines = portConfigRepository.findByLocationId(locationId);
        model.addAttribute("machines", machines);
        return "ApplicationStatus"; 
    }

}


