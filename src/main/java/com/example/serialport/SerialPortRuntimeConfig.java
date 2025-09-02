package com.example.serialport;

public class SerialPortRuntimeConfig {

    private String portName;
    private boolean rtsEnabled;
    private boolean dtrEnabled;
    private WriteLog writeLog = WriteLog.NO;
    private long delayTimeMs = 0;

    private boolean pingEnabled = false;
    private String pingIp;

    public enum WriteLog { YES, NO }

    // --- Getters and Setters ---
    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public boolean isRtsEnabled() {
        return rtsEnabled;
    }

    public void setRtsEnabled(boolean rtsEnabled) {
        this.rtsEnabled = rtsEnabled;
    }

    public boolean isDtrEnabled() {
        return dtrEnabled;
    }

    public void setDtrEnabled(boolean dtrEnabled) {
        this.dtrEnabled = dtrEnabled;
    }

    public WriteLog getWriteLog() {
        return writeLog;
    }

    public void setWriteLog(WriteLog writeLog) {
        this.writeLog = writeLog;
    }

    public long getDelayTimeMs() {
        return delayTimeMs;
    }

    public void setDelayTimeMs(long delayTimeMs) {
        this.delayTimeMs = delayTimeMs;
    }

    public boolean isPingEnabled() {
        return pingEnabled;
    }

    public void setPingEnabled(boolean pingEnabled) {
        this.pingEnabled = pingEnabled;
    }

    public String getPingIp() {
        return pingIp;
    }

    public void setPingIp(String pingIp) {
        this.pingIp = pingIp;
    }
}
