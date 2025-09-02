package com.example.mappingscreen;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lisParameter.LisParameter;
import com.example.lisParameter.LisParameterService;
import com.example.machineparameter.MachineParameter;
import com.example.machineparameter.MachineParameterService;

@Controller
@RequestMapping("/api/mapping")
public class MappingScreenController {

    @Autowired
    private MappingScreenService mappingscreenservice;

    @Autowired
    private MachineParameterService machineParameterService;

    @Autowired
    private MappingScreenRepository mappingscreenrepository;

    @Autowired
    private LisParameterService lisParameterService;

   
    @GetMapping("/mappingscreen")
    public String showMappingScreen(@RequestParam(value = "machineId", required = false) Long machineId, Model model) {
        model.addAttribute("locations", mappingscreenservice.getAllLocations());
        model.addAttribute("machines", mappingscreenservice.getAllMachines());

        List<MachineParameter> machineParameters = mappingscreenservice.getAllMachineParameters();
        List<LisParameter> lisCodeList = lisParameterService.getAllParameters();

        if (machineId != null) {
            String machineIdStr = String.valueOf(machineId);

            machineParameters = machineParameters.stream()
                    .filter(mp -> mp.getMachineId().equals(machineId))
                    .collect(Collectors.toList());

            lisCodeList = lisCodeList.stream()
                    .filter(lp -> machineIdStr.equals(lp.getMachineId()))
                    .collect(Collectors.toList());
        }

        MappingScreen defaultForm = new MappingScreen();
        defaultForm.setRoundOff("0"); // String
        defaultForm.setMultiplyBy("1"); // String
        defaultForm.setDividedBy("1"); // String
        defaultForm.setResultFormat("##.##");
        defaultForm.setDisplayOrder(String.valueOf(1)); //
        defaultForm.setActive(true);
        defaultForm.setLIMT_IsCalculated(false);
        defaultForm.setLIMT_IsDownload(false); 
        

        model.addAttribute("mappingScreen", defaultForm);
        
        model.addAttribute("machineTests", machineParameters);
        model.addAttribute("lisCodeList", lisCodeList);

        return "mapscreen";
    }

     @PostMapping("/mappingScreen")
    public String saveMappingScreen(@ModelAttribute MappingScreen form, Model model, RedirectAttributes redirectAttributes) {
    	System.out.println("==== Debugging Submitted Form Data ====");
        System.out.println("TestId: " + form.getTestId());
        System.out.println("TestName: " + form.getTestName());
        System.out.println("ParameterId: " + form.getParameterId());
        System.out.println("ParameterName: " + form.getParameterName());
        System.out.println("LocationId: " + form.getLocationId());
        System.out.println("MachineId: " + form.getMachineid()); 
        System.out.println("Machine ID: " + form.getMachineid());
        System.out.println("Location ID: " + form.getLocationId());
        
        if (form.getTestId() == null || form.getTestName() == null ||
            form.getParameterId() == null || form.getParameterName() == null ||
            form.getLocationId() == null || form.getMachineid() == null) {
            model.addAttribute("error", "Please fill all required fields.");
            return "MappingScreen";
        }

        // Fetch creation dates
        MachineParameter machine = machineParameterService.getByTestId(form.getTestId());
        LisParameter parameter = lisParameterService.getByParameterId(form.getParameterId());

        // Map to entity
        MappingScreen master = new MappingScreen();
        master.setMachineid(form.getMachineid());
        master.setLocationId(form.getLocationId());
        
        master.setTestId(form.getTestId());
        master.setTestName(form.getTestName());
        master.setParameterId(form.getParameterId());
        master.setParameterName(form.getParameterName());
        
        master.setRoundOff(form.getRoundOff());
        master.setMultiplyBy(form.getMultiplyBy());
        master.setDividedBy(form.getDividedBy()); // ✅ added
        master.setResultFormat(form.getResultFormat()); // ✅ added
        master.setDisplayOrder(form.getDisplayOrder()); // ✅ added
        
        master.setLIMT_IsDownload(Boolean.TRUE.equals(form.getLIMT_IsDownload()));
        master.setActive(Boolean.TRUE.equals(form.getActive()));
        master.setLIMT_IsCalculated(Boolean.TRUE.equals(form.getLIMT_IsCalculated()));
        
     // Set the creation datetime from machine table
        master.setMachinecreatedDateTime(
            (machine != null && machine.getCreatedDateTime() != null)
                ? machine.getCreatedDateTime()
                : LocalDateTime.now()
        );

        // Set the creation datetime from parameter table
        master.setParametercreatedDateTime(
            (parameter != null && parameter.getCreatedDateTime() != null)
                ? parameter.getCreatedDateTime()
                : LocalDateTime.now()
        );
        try {
            mappingscreenrepository.save(master);
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        redirectAttributes.addFlashAttribute("message", "Mapping saved successfully");
        
        
        return "redirect:/api/mapping/mappingscreen";
    }

    

    // Get Machine Tests for View Fragment
    @GetMapping("/getTestsByMachineview/{machineId}")
    public String getTestsByMachineView(@PathVariable Long machineId, Model model) {
        List<MachineParameter> machineTests = machineParameterService.getTestsByMachineId(machineId);
        model.addAttribute("machineTests", machineTests);
        return "fragments/machineTable :: tableRows(${machineTests})";
    }

    //  Get Machine Tests as JSON (for JS use)
    @GetMapping("/getTestsByMachine/{machineId}")
    @ResponseBody
    public List<MachineParameter> getTestsByMachine(@PathVariable Long machineId) {
        return machineParameterService.getTestsByMachineId(machineId);
    }
    
    @DeleteMapping("/deleteParameter/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteParameter(@PathVariable Long id) {
        try {
            mappingscreenrepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting: " + e.getMessage());
        }
    }
}
