package com.example.lablink.dto;

public class TestResponse {
    private boolean ok;
    private String log;
    private String token; // frontend can use to enable Save

    public TestResponse() {}
    public TestResponse(boolean ok, String log, String token) {
        this.ok = ok;
        this.log = log;
        this.token = token;
    }

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public String getLog() { return log; }
    public void setLog(String log) { this.log = log; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
