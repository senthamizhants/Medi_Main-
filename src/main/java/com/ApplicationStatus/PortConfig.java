package com.ApplicationStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "LI_RSPortConfig")
public class PortConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MachineId")
    private int machineId;

    @Column(name = "MachineName")
    private String machineName;

    @Column(name = "CommunicationType")
    private String communicationType;

    @Column(name = "Connectivity")
    private String connectivity;

    @Column(name = "port")
    private String port;

    @Column(name = "ProtocolType")
    private String protocolType;

    @Column(name = "DBType")
    private String dbType;

    @Column(name = "DBCredentials")
    private String dbCredentials;

    @Column(name = "BaudRate")
    private String baudRate;

    @Column(name = "Parity")
    private String parity;

    @Column(name = "StopBits")
    private String stopBits;

    @Column(name = "DataBits")
    private String dataBits;

    @Column(name = "X_OnOff")
    private String xOnOff;

    @Column(name = "Classname")
    private String classname;

    @Column(name = "Methodname")
    private String methodname;

    @Column(name = "isActive")
    private int isActive;

    @Column(name = "LocationId")
    private int locationId;

    // --- Getters & Setters ---
    public int getMachineId() { return machineId; }
    public void setMachineId(int machineId) { this.machineId = machineId; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getConnectivity() { return connectivity; }
    public void setConnectivity(String connectivity) { this.connectivity = connectivity; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }
}
