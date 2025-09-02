package com.example.datatracker;

import java.time.LocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
@Entity
@Table(name = "RawDataLogs")
public class DataTrackerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String machineId;
    private String machineName;
    private String protocol;
    private String sampleNumber;

    @Column(length = 5000)
    private String rawMessage;

    private LocalDateTime receivedDate;
    private boolean processed;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public String getSampleNumber() { return sampleNumber; }
    public void setSampleNumber(String sampleNumber) { this.sampleNumber = sampleNumber; }

    public String getRawMessage() { return rawMessage; }
    public void setRawMessage(String rawMessage) { this.rawMessage = rawMessage; }

    public LocalDateTime getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }

    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }
}
