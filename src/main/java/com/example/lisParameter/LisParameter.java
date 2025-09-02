package com.example.lisParameter;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "LI_LIS_Parameter")
public class LisParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIMT_ID")
    private Long id;

    @Column(name = "LocationID")
    private String locationId; 
  //  @Column(name = "LOCATIONNAME")
    //private String locationName;

    @Column(name = "LIMM_ID")
    private Long machineId;
    
   // @Column(name = "LIMM_NAME")
   // private String machineName;


    @Column(name = "LIMT_LISPARAMETERID")
    private String parameterId;

    @Column(name = "LIMT_LISPARAMETERNAME")
    private String parameterName;
    
    @Column(name = "LIMT_IsActive")
    private Integer active = 1;
   
    @Column(name = "LIS_CREATEDDTTM")
    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
        this.createdDateTime = LocalDateTime.now();
    }
    @Transient
    private String locationName;

    @Transient
    private String machineName;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public String getLocationName() {  return locationName;  }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    
   public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    } 
    public String getMachineName() {  return machineName;    }

    public void setMachineName(String machineName) {this.machineName = machineName;    }
    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
