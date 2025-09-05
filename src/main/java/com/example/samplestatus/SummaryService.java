package com.example.samplestatus;

import com.example.samplestatus.SummaryDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date; // IMPORTANT: java.sql.Date for JDBC parameters
import java.time.LocalDate;
import java.util.List;

@Service
public class SummaryService {

    private final JdbcTemplate jdbcTemplate;

    public SummaryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SummaryDto> getSummary(LocalDate startDate, LocalDate endDate) {
        final String sql = """
            SELECT 
                ISNULL(o.ProcessDate, r.ProcessDate) AS ProcessDate,
                ISNULL(o.TotalOrders, 0) AS TotalOrders,
                ISNULL(r.TotalResults, 0) AS TotalResults
            FROM
            (
                SELECT CAST(CreateDate AS DATE) AS ProcessDate,
                       COUNT(DISTINCT SampleNo) AS TotalOrders
                FROM LI_OrderMessageDetails
                WHERE CreateDate BETWEEN ? AND ?
                GROUP BY CAST(CreateDate AS DATE)
            ) o
            FULL OUTER JOIN
            (
                SELECT CAST(LIP_LastModifiedDttm AS DATE) AS ProcessDate,
                       COUNT(DISTINCT LIMO_LABID) AS TotalResults
                FROM LI_PatientResults
                WHERE LIP_LastModifiedDttm BETWEEN ? AND ?
                GROUP BY CAST(LIP_LastModifiedDttm AS DATE)
            ) r
            ON o.ProcessDate = r.ProcessDate
            ORDER BY ProcessDate;
        """;

        Object[] params = {
            Date.valueOf(startDate), Date.valueOf(endDate), // cast to SQL Date
            Date.valueOf(startDate), Date.valueOf(endDate)
        };

        return jdbcTemplate.query(sql, params, (rs, i) -> new SummaryDto(
                rs.getDate("ProcessDate").toLocalDate(),
                rs.getInt("TotalOrders"),
                rs.getInt("TotalResults")
        ));
    }
}
