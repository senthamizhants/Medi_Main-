package com.example.lablink.dto;

public class PingResponse {
    private boolean reachable;
    private String message;
    private long rttMs;

    public PingResponse() {}
    public PingResponse(boolean reachable, String message, long rttMs) {
        this.reachable = reachable;
        this.message = message;
        this.rttMs = rttMs;
    }

    public boolean isReachable() { return reachable; }
    public void setReachable(boolean reachable) { this.reachable = reachable; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getRttMs() { return rttMs; }
    public void setRttMs(long rttMs) { this.rttMs = rttMs; }
}
