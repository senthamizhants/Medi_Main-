package com.example.serialport;

public class DemoProcessor extends SerialDataProcessor {

    @Override
    public void onData(String raw, SerialPortService ctx) {
        System.out.println("Received data: " + raw);
    }
}