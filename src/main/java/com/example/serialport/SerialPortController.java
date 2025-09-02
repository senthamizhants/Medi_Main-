package com.example.serialport;

import org.springframework.web.bind.annotation.*;

import com.example.addmachine.AddMachineService;
import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SerialPortController {

    private final SerialPortService svc;
    private final SerialPortRepository repo;
    private final AddMachineService addMachineService;
       

    public SerialPortController(SerialPortService svc,
            SerialPortRepository repo,
            AddMachineService addMachineService) {
this.svc = svc;
this.repo = repo;
this.addMachineService = addMachineService;
}
   
    @GetMapping("/ports")
    public List<String> listPorts() {
        List<String> portNames = new ArrayList<>();
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            portNames.add(port.getSystemPortName());
        }
        return portNames;
    }

   /*
    @PostMapping("/open")
    public ResponseEntity<?> open(@RequestBody SerialPortConfig cfg) {
        svc.open(cfg);
        return ResponseEntity.ok().build();
    }

   
    @PostMapping("/close")
    public ResponseEntity<?> close() {
        svc.close();
        return ResponseEntity.ok().build();
    }

   
    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody String payload) {
        svc.write(payload);
        return ResponseEntity.ok().build();
    }

   
    @GetMapping("/status")
    public ResponseEntity<?> status() {
        return ResponseEntity.ok(new Object() {
            public final boolean open = svc.isOpen();
            public final SerialPortConfig config = svc.getConfig();
        });
    }
*/
    // ✅ Single testPort method (no duplicates)
  /*  @PostMapping("/testPort")
    @ResponseBody
    public Map<String, String> testPort(@RequestBody SerialPortConfig config) {
        Map<String, String> response = new HashMap<>();
        try {
            SerialPort port = SerialPort.getCommPort(config.getPortName());
            port.setBaudRate(config.getBaudRate());
            port.setNumDataBits(config.getDataBits());

            switch (config.getStopBits()) {
                case 1 -> port.setNumStopBits(SerialPort.ONE_STOP_BIT);
                case 2 -> port.setNumStopBits(SerialPort.TWO_STOP_BITS);
                case 3 -> port.setNumStopBits(SerialPort.ONE_POINT_FIVE_STOP_BITS);
                default -> port.setNumStopBits(SerialPort.ONE_STOP_BIT);
            }

            switch (config.getParity()) {
                case 1 -> port.setParity(SerialPort.ODD_PARITY);
                case 2 -> port.setParity(SerialPort.EVEN_PARITY);
                default -> port.setParity(SerialPort.NO_PARITY);
            }

            if (port.openPort()) {
                response.put("message", "✅ Connection successful on " + config.getPortName());
                port.closePort();
            } else {
                response.put("message", "❌ Failed to open " + config.getPortName());
            }
        } catch (Exception e) {
            response.put("message", "⚠️ Error: " + e.getMessage());
        }
        return response;
    }
*/
   
   
/*    @PostMapping("/saveRs232Config")
    public String saveConfig(@ModelAttribute("rs232") SerialPortConfig config, Model model) {
        System.out.println("Saving config for port: " + config.getPortName());
        model.addAttribute("message", "Config saved successfully!");
        return "fragments/configRS :: configRSFormFragment";
    } */

      
    @GetMapping("/checkPortStatus")
    @ResponseBody
    public String checkPortStatus(@RequestParam String portName) {
        try {
            SerialPort port = SerialPort.getCommPort(portName);
            port.setBaudRate(9600); 
            if (port.openPort()) {
                port.closePort();
                return "CONNECTED (" + portName + ")";
            } else {
                return "FAILED (" + portName + ")";
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
 /* not used
    @PostMapping("/saveRs232Config")
    public String saveRs232Config(@ModelAttribute SerialPortConfig config) {
        addMachineService.saveMachineWithConfig(config);
        return "redirect:/home";
    }
    */

}
