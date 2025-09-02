package com.example.mappingscreen;
import java.time.LocalDateTime;


import javax.persistence.*;

@Entity
@Table(name = "LI_MachineTestMaster")
public class MappingScreen {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIMT_ID")
    private Long id;
	
	@Column(name = "LIMM_ID")
	private Long machineid;
	
    @Column(name = "LIMT_TESTID")
    private String testId;

    @Column(name = "LIMT_TESTNAME")
    private String testName;

    @Column(name = "LIMT_LISPARAMETERID")
    private String parameterId;

    @Column(name = "LIMT_LISPARAMETERNAME")
    private String parameterName;

       
    @Column(name = "LIMT_ROUNDOFF")
    private String roundOff;

    @Column(name = "LIMT_MULTIPLYBY")
    private String multiplyBy;

    @Column(name = "LIMT_DIVIDEBY")
    private String dividedBy;

    @Column(name = "LIMT_FORMAT")
    private String resultFormat;

    @Column(name = "LIMT_DISPLAYORDER")
    private String displayOrder;
    
    
    @Column(name = "LocationID")
    private Long locationId;

    private boolean LIMT_IsDownload;
    
    @Column(name = "LIMT_IsActive")
    private boolean active;
   
    private boolean LIMT_IsCalculated;
    
    @Column(name = "LIMT_CreatedDttm")
    private LocalDateTime machinecreatedDateTime;

  
   @Column(name = "LIS_CREATEDDTTM")
private LocalDateTime parametercreatedDateTime;

   @Column(name = "UpdateFlg")
   private Integer updateFlag;

   @Column(name = "TransferFlg")
   private Integer transferFlag;

   @Column(name = "TransferDttm")
   private LocalDateTime transferDateTime;

   @Column(name = "LIS_CREATEDBY")
   private String createdBy;

   @Column(name = "LIS_MODIFIEDBY")
   private String modifiedBy;

   @Column(name = "LIS_MODIFIEDDTTM")
   private LocalDateTime modifiedDateTime;

 @PrePersist
protected void onCreate() {
    this.machinecreatedDateTime = LocalDateTime.now();
    this.parametercreatedDateTime = LocalDateTime.now();
}
   
	    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMachineid() {
        return machineid;
    }

    public void setMachineid(Long machineid) {
        this.machineid = machineid;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

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

    public String getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(String roundOff) {
        this.roundOff = roundOff;
    }

    public String getMultiplyBy() {
        return multiplyBy;
    }

    public void setMultiplyBy(String multiplyBy) {
        this.multiplyBy = multiplyBy;
    }

    public String getDividedBy() {
        return dividedBy;
    }

    public void setDividedBy(String dividedBy) {
        this.dividedBy = dividedBy;
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public void setResultFormat(String resultFormat) {
        this.resultFormat = resultFormat;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public boolean getLIMT_IsDownload() {
        return LIMT_IsDownload;
    }

    public void setLIMT_IsDownload(boolean LIMT_IsDownload) {
        this.LIMT_IsDownload = LIMT_IsDownload;
    }

    public boolean getLIMT_IsCalculated() {
        return LIMT_IsCalculated;
    }

    public void setLIMT_IsCalculated(boolean LIMT_IsCalculated) {
        this.LIMT_IsCalculated = LIMT_IsCalculated;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public LocalDateTime getMachinecreatedDateTime() {
        return machinecreatedDateTime;
    }

    public void setMachinecreatedDateTime(LocalDateTime machinecreatedDateTime) {
        this.machinecreatedDateTime = machinecreatedDateTime;
    }

    public LocalDateTime getParametercreatedDateTime() {
        return parametercreatedDateTime;
    }

    public void setParametercreatedDateTime(LocalDateTime parametercreatedDateTime) {
        this.parametercreatedDateTime = parametercreatedDateTime;
    }
    public Integer getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Integer updateFlag) {
        this.updateFlag = updateFlag;
    }

    public Integer getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(Integer transferFlag) {
        this.transferFlag = transferFlag;
    }

    public LocalDateTime getTransferDateTime() {
        return transferDateTime;
    }

    public void setTransferDateTime(LocalDateTime transferDateTime) {
        this.transferDateTime = transferDateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}
