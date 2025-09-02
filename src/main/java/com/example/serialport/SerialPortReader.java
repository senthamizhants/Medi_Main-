package com.example.serialport;


import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;
import java.util.List;


public class SerialPortReader {
	public List<String> getAvailablePorts() {
        List<String> ports = new ArrayList<>();
        SerialPort[] portList = SerialPort.getCommPorts();
        for (SerialPort port : portList) {
            ports.add(port.getSystemPortName());
        }
        return ports;
    }
}
