package com.example.samplestatus;

import com.example.samplestatus.SummaryDto;
import com.example.samplestatus.SummaryService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService; // constructor injection prevents nulls
    }

    @GetMapping("/summary")
    public String getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<SummaryDto> summaryList = summaryService.getSummary(startDate, endDate);
        model.addAttribute("summaryList", summaryList);

        // 👇 prevent null
        model.addAttribute("totalPages", summaryList.isEmpty() ? 1 : 5); 

        return "SampleStatus";
    }


    private LocalDate parseOrDefault(String text, LocalDate def) {
        if (text == null || text.isBlank()) return def;
        try {
            // matches <input type="date"> default (yyyy-MM-dd)
            return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ex) {
            return def; // fallback gracefully instead of 400
        }
    }
}
