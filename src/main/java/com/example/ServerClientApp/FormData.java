package com.example.ServerClientApp;

public class FormData {
    private String ip;
    private String port;
    private String serverName;
    private String dbType;
    private String dbUser;
    private String dbPass;
    private String machineId;
    private String clientIp;
    private String machineName;
    private String pingServerIp;

    // Getters and setters for all fields
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public String getDbUser() { return dbUser; }
    public void setDbUser(String dbUser) { this.dbUser = dbUser; }

    public String getDbPass() { return dbPass; }
    public void setDbPass(String dbPass) { this.dbPass = dbPass; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }

    public String getPingServerIp() { return pingServerIp; }
    public void setPingServerIp(String pingServerIp) { this.pingServerIp = pingServerIp; }
}
