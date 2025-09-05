package com.example.serialport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id; 



@Entity
@Table(name = "LI_RSPortConfig")
public class SerialPortConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MachineId")
    private Integer machineId; 
   
    
    @Column(name = "LocationID")
    private Long locationId; 
    
    @Column(name = "LOCATIONNAME")
    private String locationName;

    
    @Column(name = "CommunicationType")
    private String communication;

    @Column(name = "Connectivity")
    private String connectivity;

    @Column(name = "Methodname")
    private String methodname;

    @Column(name = "MachineName", nullable = false)
    private String machineName;

    /*
    @Column(name = "BaudRate")
    private int baudRate;  */

    @Column(name = "BaudRate")
    private String baudRate; 

    @Column(name = "Parity")
    private String parity;
    
    /*   
    @Column(name = "StopBits")
    private int stopBits;       // changed from String to int

    @Column(name = "DataBits")
    private int dataBits;     
*/
    @Column(name = "StopBits")
    private String stopBits;

    @Column(name = "DataBits")
    private String dataBits; 

    @Column(name = "ProtocolType")
    private String protocolType;

    @Column(name = "DBType")
    private String dbType;

    @Column(name = "DBCredentials")
    private String dbCredentials;

    @Column(name = "Port")
    private String port;

    @Column(name = "X_OnOff")
    private String xonoff;

    @Column(name = "Classname")
    private String classname;

    @Column(name = "IsActive")
    private int isActive = 1;   // default 1

    // 👉 Getters & Setters
    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public String getLocationName() {  return locationName;  }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    
    public Integer getMachineId() { return machineId; }
    public void setMachineId(Integer machineId) { this.machineId = machineId; }

    public String getCommunication() { return communication; }
    public void setCommunication(String communication) { this.communication = communication; }

    public String getConnectivity() { return connectivity; }
    public void setConnectivity(String connectivity) { this.connectivity = connectivity; }

    public String getMethodname() { return methodname; }
    public void setMethodname(String methodname) { this.methodname = methodname; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    /*   public int getBaudRate() { return baudRate; }
    public void setBaudRate(int baudRate) { this.baudRate = baudRate; }*/
  
    public String getBaudRate() { return baudRate; }
    public void setBaudRate(String baudRate) { this.baudRate = baudRate; } 

    public String getParity() { return parity; }
    
    public void setParity(String parity) { this.parity = parity; }
    /*
    public int getStopBits() { return stopBits; }
    
    
    public void setStopBits(int stopBits) { this.stopBits = stopBits; }

    public int getDataBits() { return dataBits; }
    public void setDataBits(int dataBits) { this.dataBits = dataBits; } */
    
 
    public String getStopBits() { return stopBits; }
    public void setStopBits(String stopBits) { this.stopBits = stopBits; }

    public String getDataBits() { return dataBits; }
    public void setDataBits(String dataBits) { this.dataBits = dataBits; } 

    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public String getDbCredentials() { return dbCredentials; }
    public void setDbCredentials(String dbCredentials) { this.dbCredentials = dbCredentials; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getXonoff() { return xonoff; }
    public void setXonoff(String xonoff) { this.xonoff = xonoff; }

    public String getClassname() { return classname; }
    public void setClassname(String classname) { this.classname = classname; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
}


