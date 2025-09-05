package com.example.machineparameter;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MachineParameterController {

    @Autowired
    private MachineParameterService machineParameterService;

    @PostMapping("/savemachineCode")
    public String saveOrUpdateMachineParam(@ModelAttribute("machineParam") MachineParameter param,
            @RequestParam(name = "_action", required = false, defaultValue = "save") String action,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {
if ("save".equals(action)) {
machineParameterService.save(param);
redirectAttributes.addFlashAttribute("message", "✅ Saved successfully");
} else if ("update".equals(action)) {
machineParameterService.update(param);
redirectAttributes.addFlashAttribute("message", "✅ Updated successfully");
}
return "redirect:/machineCode?page=" + page;
}
    @DeleteMapping("/deleteMachineParameter/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMachineParameter(@PathVariable Long id) {
        try {
            machineParameterService.deleteById(id);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting entry");
        }
    } 
    @GetMapping("/deleteMachineParam/{id}")
    public String deleteMachineParam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        machineParameterService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "✅ Deleted successfully.");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/machineCode";
    }
    
    
    @GetMapping("/searchMachineCode")
    public String searchMachineCodeRows(@RequestParam("keyword") String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        Model model) {
        int pageSize = 8;
        Page<MachineParameter> searchResults = 
            keyword.trim().isEmpty() 
            ? machineParameterService.getPaginated(page, pageSize)
            : machineParameterService.search(keyword, page, pageSize);

        model.addAttribute("machineTestMappings", searchResults.getContent());
        model.addAttribute("currentPage", page);
        return "fragments/machineTableFragment :: tableRows";
    }
    
    
    
    @GetMapping("/machinesByLocation/{locationId}")
    @ResponseBody
    public List<MachineParameter> getMachinesByLocation(@PathVariable Long locationId) {
        return machineParameterService.getMachinesByLocation(locationId);
    }

    @GetMapping("/testsByMachine")
    public String getTestsByMachine(@RequestParam Long machineId,
                                    @RequestParam Long locationId,
                                    Model model) {
        List<MachineParameter> tests = machineParameterService.getTestsByMachineAndLocation(machineId, locationId);
        model.addAttribute("machineTestMappings", tests);
        return "fragments/machineTableFragment :: tableRows";
    }

}