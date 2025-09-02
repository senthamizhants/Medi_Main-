package com.example.login;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.addmachine.AddMachineService;
import com.example.lisParameter.LisParameter;
import com.example.lisParameter.LisParameterService;
import com.example.location.Location;
import com.example.location.LocationService;
import com.example.machine.Machine;
import com.example.machine.MachineService;
import com.example.machineparameter.MachineParameter;
import com.example.machineparameter.MachineParameterService;
import com.example.mappingscreen.MappingScreen;
import com.example.serialport.SerialPortConfig;
import org.springframework.web.bind.annotation.GetMapping;
import com.fazecast.jSerialComm.SerialPort;


@Controller

public class LoginController {

	@Autowired
	private AddMachineService addMachineService;
    @Autowired
    private LoginService loginService;

    @GetMapping("/")

    public String showLoginPage() {

        return "LogPage"; 

    }

    @GetMapping("/login")

    public String showLoginpage() {

        return "LogPage";    

    }
    
    @GetMapping("/ServiceStatus")

    public String showServiceStatuspage() {

        return "ServiceStatus";    

    }



    @PostMapping("/login")

    public String doLogin(@RequestParam String username, 
                          @RequestParam String password, 
                          Model model) {
        boolean success = loginService.validateUser(username, password);
        if (success) {
            return "redirect:/home";
            

        } else {
            model.addAttribute("error", "Invalid credentials");
            return "LogPage";
        }
    }
   @GetMapping("/home")

   public String home(HttpServletResponse response, Model model) {

       // Prevent caching so browser won't show "resubmit form" warning

       response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
       response.setHeader("Pragma", "no-cache");
       response.setHeader("Expires", "0");
       



       // Load machines for the HomePage

       List<SerialPortConfig> machines = addMachineService.getAllMachines();
       model.addAttribute("machines", machines);
       return "HomePage";  

   }

   /*    

@GetMapping("/home")

public String home() {

return "HomePage";

}



@GetMapping("/add-machine")

public String showForm(Model model) {

    model.addAttribute("machine", new Machine());

    model.addAttribute("machineList", machineService.getAllMachines());

    return "AddMachine";

} */

/*

@GetMapping("/add-machine")

public String showForm(Model model) {

    model.addAttribute("machine", new Machine());

    return "AddMachine";

}

/*@PostMapping("/add-machine")

public String addMachine(@ModelAttribute Machine machine) {

    machineService.saveMachineWithConfig(machine); 

    return "redirect:/home?success";

}

 */







@GetMapping("/applicationStatus")

public String showApplicationStatus(Model model) {

    List<SerialPortConfig> machines = addMachineService.getAllMachines();

    model.addAttribute("machines", machines);

    return "ApplicationStatus";  

}



@RequestMapping("/fragments")

public class FragmentController {



    @GetMapping("/machineCode")

    public String getMachineCodeFragment() {

        return "fragments/MachineCode"; 

    }



    @GetMapping("/lisparameterCode")

    public String getLisParameterFragment() {

        return "fragments/lisparametercodepage";

    }



    @GetMapping("/mappingScreen")

    public String getMappingScreenFragment() {

        return "fragments/mappingScreen";

    }

}



@Autowired

private LocationService locationService;



@Autowired

private MachineService machineService;



@Autowired

private MachineParameterService machineParameterService;



@GetMapping("/machineCode")

public String showMachineCodeForm(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "") String search,

        Model model) {



    int pageSize = 8;

    Page<MachineParameter> pagedData;



    if (!search.trim().isEmpty()) {

        pagedData = machineParameterService.searchMachineParameters(search, page, pageSize);

    } else {

        pagedData = machineParameterService.getPaginated(page, pageSize);

    }

    model.addAttribute("machines", machineParameterService.getDistinctMachines());

   model.addAttribute("machineParam", new MachineParameter());

    model.addAttribute("locations", locationService.getAllLocations());

    model.addAttribute("machines", machineService.getAllMachines());



    // IMPORTANT: This name must match what the fragment expects

    model.addAttribute("machineTests", pagedData.getContent());

    model.addAttribute("currentPage", page);

    model.addAttribute("totalPages", pagedData.getTotalPages());

    model.addAttribute("search", search);



    return "machinecodepage";

}



@Autowired

private LisParameterService lisParameterService;



@GetMapping("/mappingScreen")

public String showMappingScreen(Model model) {

    List<Location> locations = locationService.getAllLocations();

    List<Machine> machines = machineService.getAllMachines();



    List<MachineParameter> machineTests = machineParameterService.getTestsByMachineId(machines.get(0).getMachineId());

    List<LisParameter> lisCodeList = lisParameterService.getAllParameters();



    MappingScreen defaultForm = new MappingScreen();

    defaultForm.setRoundOff("0"); 

    defaultForm.setMultiplyBy("1"); 

    defaultForm.setDividedBy("1"); 

    defaultForm.setResultFormat("##.##");

    defaultForm.setDisplayOrder(String.valueOf(1));

    defaultForm.setActive(true);

    defaultForm.setLIMT_IsCalculated(false);

    defaultForm.setLIMT_IsDownload(false);  

    

    model.addAttribute("mappingScreen", defaultForm);

    

    model.addAttribute("locations", locations);

    model.addAttribute("machines", machines);

    model.addAttribute("machineTests", machineTests);

    model.addAttribute("lisCodeList", lisCodeList);    

  

    return "mapscreen"; 

}

/*

@GetMapping("/lisparameterCode")

public String showLisParameterPage(

        Model model,

        @RequestParam(name = "page", defaultValue = "0") int page,

        @RequestParam(name = "search", required = false) String search) {



    Pageable pageable = PageRequest.of(page, 8);



    Page<LisParameter> result;



    if (search != null && !search.isEmpty()) {

        result = lisParameterRepository.findByParameterIdContainingIgnoreCaseOrParameterNameContainingIgnoreCase(search, search, pageable);

        model.addAttribute("search", search);

    } else {

        result = lisParameterRepository.findAll(pageable);

        model.addAttribute("search", "");

    }



    // ✅ Required line for form binding

    model.addAttribute("lisCode", new LisParameter());



    model.addAttribute("locations", locationService.getAllLocations());

    model.addAttribute("machines", machineService.getAllMachines());

    model.addAttribute("lisCodeList", result.getContent());

    model.addAttribute("currentPage", result.getNumber());

    model.addAttribute("totalPages", result.getTotalPages());

        return "LIS_ParameterCode";

}   

@GetMapping("/configRS")

public String loadRs232Config() {

	

    return "configRS"; // maps to configRS.html in /static or templates

}*/



@GetMapping("/configRS")

public String loadRs232Config(@RequestParam("machineName") String machineName, Model model) {

    

    SerialPort[] ports = SerialPort.getCommPorts();

    String[] portNames = new String[ports.length];

    for (int i = 0; i < ports.length; i++) {

        portNames[i] = ports[i].getSystemPortName();

    }   

    SerialPortConfig config = new SerialPortConfig();

    config.setMachineName(machineName);



    model.addAttribute("ports", portNames);

    model.addAttribute("rs232", config);



    return "fragments/configRS :: configRSFormFragment";

}

/*

@GetMapping("/client-config")

public String loadTcpIpConfig(@RequestParam("machineName") String machineName, Model model) {

	 SerialPortConfig config = new SerialPortConfig();

	    config.setMachineName(machineName);

	   

	    model.addAttribute("rs232", config);



	 return "fragments/index :: tcpipFormFragment";

    //return "index"; 

}

*/

@GetMapping("/mappingDetail")

public String showMappingDetail() {

	 return "redirect:/machineCode";

}

@GetMapping("/mappingDetails")

public String showmappingPage() {

    return "MappingDetails";  

}

@PostMapping("/mappingForm")

public String handleSubmit(@RequestParam("_action") String action) {

    if ("cancel".equals(action)) {

        return "redirect:/home";  

    }

	return action;

}



@GetMapping("/logout")

public String logout(HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession(false);

    if (session != null) {

        session.invalidate();

    }

    return "redirect:/LogPage";

}

}