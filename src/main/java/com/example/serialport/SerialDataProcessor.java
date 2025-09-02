package com.example.serialport;

public abstract class SerialDataProcessor {
    public abstract void onData(String raw, SerialPortService ctx);
}
