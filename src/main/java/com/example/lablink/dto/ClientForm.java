package com.example.lablink.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.lablink.domain.ProtocolType;

public class ClientForm {

    @NotBlank(message = "Host IP is required")
    private String hostIp;

    @NotNull(message = "Port is required")
    @Min(value = 1, message = "Port must be between 1 and 65535")
    @Max(value = 65535, message = "Port must be between 1 and 65535")
    private Integer port;

    @NotBlank(message = "Machine name is required")
    private String machineName;

    @NotBlank(message = "Machine ID is required")
    private String machineId;

    @NotBlank(message = "Database type is required")
    private String databaseType;

    @NotBlank(message = "DB credentials are required")
    private String dbCredentials;

    @NotNull(message = "Protocol type is required")
    private ProtocolType protocolType;

    @NotBlank(message = "Server IP to ping is required")
    private String pingIp;

    public String getHostIp() { return hostIp; }
    public void setHostIp(String hostIp) { this.hostIp = hostIp; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }

    public String getDbCredentials() { return dbCredentials; }
    public void setDbCredentials(String dbCredentials) { this.dbCredentials = dbCredentials; }

    public ProtocolType getProtocolType() { return protocolType; }
    public void setProtocolType(ProtocolType protocolType) { this.protocolType = protocolType; }

    public String getPingIp() { return pingIp; }
    public void setPingIp(String pingIp) { this.pingIp = pingIp; }
}
