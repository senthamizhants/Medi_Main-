package com.example.samplestatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = "LI_OrderMessageDetails")
public class DetailsTable {

    @Id
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MSampleNo")
    private String mSampleNo;

    @Column(name = "SampleNo")
    private String sampleNo;

    @Column(name = "ParameterID")
    private String parameterID;

    @Column(name = "ParameterName")
    private String parameterName;

    @Column(name = "Name")
    private String name;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "MRN")
    private String mrn;

    @Column(name = "CreateDate")
    private LocalDateTime createDate;

    @Column(name = "acceptanceDate")
    private LocalDateTime acceptanceDate;

    @Column(name = "LocationID")
    private String locationID;

    @Column(name = "OrderUpdateFlag")
    private String orderUpdateFlag;

    public DetailsTable() {} // JPA needs no-arg constructor

    // Getters and setters (generate via IDE)
    public Integer getId() { return id; }
    public String getMSampleNo() { return mSampleNo; }
    public String getSampleNo() { return sampleNo; }
    public String getParameterID() { return parameterID; }
    public String getParameterName() { return parameterName; }
    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public Integer getAge() { return age; }
    public String getSex() { return sex; }
    public String getMrn() { return mrn; }
    public LocalDateTime getCreateDate() { return createDate; }
    public LocalDateTime getAcceptanceDate() { return acceptanceDate; }
    public String getLocationID() { return locationID; }
    public String getOrderUpdateFlag() { return orderUpdateFlag; }

    public void setId(Integer id) { this.id = id; }
    public void setMSampleNo(String mSampleNo) { this.mSampleNo = mSampleNo; }
    public void setSampleNo(String sampleNo) { this.sampleNo = sampleNo; }
    public void setParameterID(String parameterID) { this.parameterID = parameterID; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    public void setName(String name) { this.name = name; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setAge(Integer age) { this.age = age; }
    public void setSex(String sex) { this.sex = sex; }
    public void setMrn(String mrn) { this.mrn = mrn; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public void setAcceptanceDate(LocalDateTime acceptanceDate) { this.acceptanceDate = acceptanceDate; }
    public void setLocationID(String locationID) { this.locationID = locationID; }
    public void setOrderUpdateFlag(String orderUpdateFlag) { this.orderUpdateFlag = orderUpdateFlag; }
}
