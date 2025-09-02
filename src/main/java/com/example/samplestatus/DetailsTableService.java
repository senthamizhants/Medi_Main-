package com.example.samplestatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DetailsTableService {

    @Autowired
    private DetailsTableRepository repository;

    public Page<DetailsTable> getTodayDataPaginated(int page, int size) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusSeconds(1);

        return repository.findByCreateDateBetween(
                startOfDay, endOfDay,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"))
        );
    }
}
