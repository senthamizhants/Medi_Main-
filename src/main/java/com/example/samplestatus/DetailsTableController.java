package com.example.samplestatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.tools.rmi.Sample;

import javax.persistence.criteria.Predicate; 

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DetailsTableController {

    @Autowired
    private DetailsTableRepository repository;

    @GetMapping("/orders")
    public String showOrders(
            @RequestParam(name = "mrn", required = false) String mrn,
            @RequestParam(name = "sampleNo", required = false) String sampleNo,
            @RequestParam(name = "startDate", required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "summary") String activeTab,
            Model model) {

        Specification<DetailsTable> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (mrn != null && !mrn.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("mrn")), "%" + mrn.toLowerCase() + "%"));
            }
            if (sampleNo != null && !sampleNo.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("mSampleNo")), "%" + sampleNo.toLowerCase() + "%"));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createDate"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), endDate.atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<DetailsTable> ordersPage = repository.findAll(spec, PageRequest.of(page, size));
        
 

        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("count", ordersPage.getTotalElements());

        model.addAttribute("mrn", mrn);
        model.addAttribute("sampleNo", sampleNo);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        model.addAttribute("activeTab", activeTab);

        return "SampleStatus";
        
    }
    
}
