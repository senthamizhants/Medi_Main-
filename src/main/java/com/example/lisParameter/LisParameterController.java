package com.example.lisParameter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.location.LocationService;
import com.example.machine.MachineService;

@Controller
public class LisParameterController {

	 @Autowired
	    private LisParameterRepository repository;

    @Autowired
    private LisParameterService service;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MachineService machineService;

    @GetMapping("/lisparameterCode")
    public String showForm(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<LisParameter> result = (search != null && !search.isEmpty())
                ? service.search(search, pageable)
                : service.findAll(pageable);

        for (LisParameter param : result.getContent()) {
            String locId = param.getLocationId();  // String
            Long machId = param.getMachineId();    // Long

            // ✅ This block replaces your existing line for location name
            try {
                Long locationId = (locId != null) ? Long.parseLong(locId) : null;
                param.setLocationName(locationId != null ? locationService.getLocationNameById(locationId) : "");
            } catch (NumberFormatException e) {
                param.setLocationName("Invalid ID");
            }

            param.setMachineName(machId != null ? machineService.getMachineNameById(machId) : "");
        }

        model.addAttribute("lisCode", new LisParameter());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("machines", machineService.getAllMachines());
        model.addAttribute("lisCodeList", result.getContent());
        model.addAttribute("currentPage", result.getNumber());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("search", search);

        return "lisparametercodepage";
    }

  /*
    @PostMapping("/lisparameterCodesave")
    public String saveOrUpdateLisCode(@ModelAttribute("lisCode") LisParameter lisCode,
                                      @RequestParam(name = "_action", defaultValue = "save") String action,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "search", required = false) String search,
                                      RedirectAttributes redirectAttributes) {
        if ("update".equalsIgnoreCase(action) && lisCode.getId() != null) {
            service.update(lisCode);
            redirectAttributes.addFlashAttribute("message", "Updated successfully.");
        } else {
            service.save(lisCode);
            redirectAttributes.addFlashAttribute("message", "Saved successfully.");
        }

        redirectAttributes.addAttribute("page", page);
        if (search != null) {
            redirectAttributes.addAttribute("search", search);
        }

        return "redirect:/lisparameterCode";
    } */
    
    @PostMapping("/lisparameterCodesave")
    public String saveOrUpdateLisCode(@ModelAttribute("lisCode") LisParameter lisCode,
                                      @RequestParam(name = "_action", defaultValue = "save") String action,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "search", required = false) String search,
                                      RedirectAttributes redirectAttributes) {

        // Check duplicate only on save (not update)
        if (!"update".equalsIgnoreCase(action) &&
            service.existsByParameterId(lisCode.getParameterId())) {

            redirectAttributes.addFlashAttribute("message", 
                "⚠ Cannot save. Parameter ID '" + lisCode.getParameterId() + "' already exists.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            redirectAttributes.addAttribute("page", page);
            if (search != null) {
                redirectAttributes.addAttribute("search", search);
            }
            return "redirect:/lisparameterCode";
        }

        if ("update".equalsIgnoreCase(action) && lisCode.getId() != null) {
            service.update(lisCode);
            redirectAttributes.addFlashAttribute("message", "✅ Updated successfully.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } else {
            service.save(lisCode);
            redirectAttributes.addFlashAttribute("message", "✅ Saved successfully.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        }

        redirectAttributes.addAttribute("page", page);
        if (search != null) {
            redirectAttributes.addAttribute("search", search);
        }

        return "redirect:/lisparameterCode";
    }
    
    @PostMapping("/lisparameterCode/delete/{id}")
    public String deleteLisParameter(@PathVariable("id") Long id,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Deleted successfully.");
        return "redirect:/lisparameterCode?page=" + page;
    }
    
    @GetMapping("/lisparameterCode/check-duplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam String parameterId) {
        return service.existsByParameterId(parameterId);
    }
}