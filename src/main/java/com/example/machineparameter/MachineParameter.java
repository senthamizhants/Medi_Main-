package com.example.machineparameter;



import java.time.LocalDateTime;

import javax.persistence.*;



@Entity
@Table(name = "LI_MachineParameter")
public class MachineParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIMP_ID")  
    private Long id;

    @Column(name = "LOCATIONID")
    private Long locationId;
    
    @Column(name = "LOCATIONNAME")
    private String locationName;

    @Column(name = "LIMM_ID")
    private Long machineId;
    
    @Column(name = "LIMM_NAME")
    private String machineName;


    @Column(name = "LIMT_TestID")
    private String testId;

    @Column(name = "LIMT_TestName")
    private String testName;
    
 
    @Column(name = "LIMT_ACTIVE")
    private Integer active = 1;

   
    @Column(name = "LIMT_Createddttm")
    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
        this.createdDateTime = LocalDateTime.now();
    }
    
    public MachineParameter() {
        // Default no-args constructor
    }

    // ✅ Add this custom constructor here
    public MachineParameter(Long machineId, String machineName) {
        this.machineId = machineId;
        this.machineName = machineName;
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public String getLocationName() {  return locationName;  }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    

    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public String getMachineName() {  return machineName;    }

    public void setMachineName(String machineName) {this.machineName = machineName;    }

    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    
    public int getActive() {
        return active != null ? active : 0; // 👈 safe default fallback
    }
    public void setActive(Integer active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    public void setCreatedDateTime(LocalDateTime createdDateTime) { this.createdDateTime = createdDateTime; }
   
}
