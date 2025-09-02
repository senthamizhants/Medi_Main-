package com.example.datatracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class DataTrackerController {

    private final DataTrackerRepository rawRepo;

    public DataTrackerController(DataTrackerRepository rawRepo) {
        this.rawRepo = rawRepo;
    }

    // Initial load
    @GetMapping("/raw-data")
    public String showPage(Model model) {
        model.addAttribute("machines", rawRepo.findDistinctMachineNames());
        model.addAttribute("rawMessages", "");
        model.addAttribute("selectedMachine", null);
        model.addAttribute("selectedProtocol", null);
        model.addAttribute("fromDate", null);
        model.addAttribute("toDate", null);
        model.addAttribute("sampleNumber", null);
        return "raw-data";
    }

    // Protocols by machine
    @GetMapping("/protocols-by-machine")
    @ResponseBody
    public List<String> getProtocolsByMachine(@RequestParam String machineName) {
        return rawRepo.findProtocolsByMachine(machineName);
    }

    // Search
    @GetMapping("/raw-data/search")
    public String searchRawData(@RequestParam(required = false) String machineName,
                                @RequestParam(required = false) String protocol,
                                @RequestParam(required = false) String fromDate,
                                @RequestParam(required = false) String toDate,
                                @RequestParam(required = false) String sampleNumber,
                                Model model) {

        LocalDateTime from = null;
        LocalDateTime to = null;

        if (fromDate != null && !fromDate.isEmpty()) {
            from = LocalDate.parse(fromDate).atStartOfDay();
        }
        if (toDate != null && !toDate.isEmpty()) {
            to = LocalDate.parse(toDate).atTime(23, 59, 59);
        }

        List<DataTrackerEntity> results = rawRepo.searchRawData(machineName, protocol, from, to);

        StringBuilder sb = new StringBuilder();
        for (DataTrackerEntity r : results) {
            if (sampleNumber == null || r.getRawMessage().contains(sampleNumber)) {
                sb.append("<div class='block'>");  // ✅ Start block

                String[] lines = r.getRawMessage().split("\\r?\\n");
                for (String line : lines) {
                    String dateStr = r.getReceivedDate()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

                    sb.append("<span style='color:green;'>")   
                      .append(dateStr)
                      .append("</span> : <span style='color:#007bff;'>")  
                      .append(line)
                      .append("</span><br>");
                }

                sb.append("</div><br>");  // ✅ End block
            }
        }

        // Pass data to Thymeleaf
        model.addAttribute("rawMessages", sb.toString());
        model.addAttribute("machines", rawRepo.findDistinctMachineNames());
        model.addAttribute("selectedMachine", machineName);
        model.addAttribute("selectedProtocol", protocol);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("sampleNumber", sampleNumber);

        return "raw-data";
    }
}
