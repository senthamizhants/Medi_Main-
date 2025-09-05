package com.example.samplestatus;

import java.time.LocalDate;

public class SummaryDto {
    private final LocalDate processDate;
    private final int totalOrders;
    private final int totalResults;

    public SummaryDto(LocalDate processDate, int totalOrders, int totalResults) {
        this.processDate = processDate;
        this.totalOrders = totalOrders;
        this.totalResults = totalResults;
    }

    public LocalDate getProcessDate() { return processDate; }
    public int getTotalOrders() { return totalOrders; }
    public int getTotalResults() { return totalResults; }
}
