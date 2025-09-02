package com.example.lablink.domain;

import java.time.Instant;
import javax.persistence.*;


@Entity
public class ConnectionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConnectionMode mode;

    private String host;
    private Integer port;

    private String machineName;
    private String machineId;

    private String dbType;
    private String dbCredentials;

    @Enumerated(EnumType.STRING)
    private ProtocolType protocol;

    private Instant createdAt = Instant.now();

    private String status = "NEW";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ConnectionMode getMode() { return mode; }
    public void setMode(ConnectionMode mode) { this.mode = mode; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public String getDbCredentials() { return dbCredentials; }
    public void setDbCredentials(String dbCredentials) { this.dbCredentials = dbCredentials; }

    public ProtocolType getProtocol() { return protocol; }
    public void setProtocol(ProtocolType protocol) { this.protocol = protocol; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
