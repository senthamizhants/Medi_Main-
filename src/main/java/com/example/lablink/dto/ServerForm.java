package com.example.lablink.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import com.example.lablink.domain.ProtocolType;

public class ServerForm {

    @NotBlank(message = "Server IP is required")
    private String serverIp;

    @NotNull(message = "Port is required")
    @Min(value = 1, message = "Port must be between 1 and 65535")
    @Max(value = 65535, message = "Port must be between 1 and 65535")
    private Integer port;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Machine ID is required")
    private String machineId;

    @NotBlank(message = "Database type is required")
    private String databaseType;

    @NotBlank(message = "DB credentials are required")
    private String dbCredentials;

    @NotNull(message = "Protocol type is required")
    private ProtocolType protocolType;

    @NotBlank(message = "Client IP is required")
    private String clientIp;

    public String getServerIp() { return serverIp; }
    public void setServerIp(String serverIp) { this.serverIp = serverIp; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }

    public String getDbCredentials() { return dbCredentials; }
    public void setDbCredentials(String dbCredentials) { this.dbCredentials = dbCredentials; }

    public ProtocolType getProtocolType() { return protocolType; }
    public void setProtocolType(ProtocolType protocolType) { this.protocolType = protocolType; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
