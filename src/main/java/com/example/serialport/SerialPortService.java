package com.example.serialport;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

@Service
public class SerialPortService {

	 public void openPort(SerialPortConfig dbConfig, SerialPortRuntimeConfig runtimeConfig) {
	        SerialPort port = SerialPort.getCommPort(dbConfig.getPort());

	        // Baud rate
	        port.setBaudRate(Integer.parseInt(dbConfig.getBaudRate()));

	        // Data bits
	        port.setNumDataBits(Integer.parseInt(dbConfig.getDataBits()));

	        // Stop bits
	        port.setNumStopBits(parseStopBits(dbConfig.getStopBits()));

	        // Parity
	        port.setParity(parseParity(dbConfig.getParity()));

	        // Apply runtime options
	        if (runtimeConfig.isRtsEnabled()) {
	            port.setRTS();   // enable RTS
	        } else {
	            port.clearRTS(); // disable RTS
	        }

	        if (runtimeConfig.isDtrEnabled()) {
	            port.setDTR();   // enable DTR
	        } else {
	            port.clearDTR(); // disable DTR
	        }

	        if (port.openPort()) {
	            System.out.println("✅ Port opened: " + dbConfig.getPort());

	            if (runtimeConfig.isPingEnabled()) {
	                System.out.println("Pinging IP: " + runtimeConfig.getPingIp());
	                // TODO: implement ping
	            }

	        } else {
	            System.out.println("❌ Failed to open port: " + dbConfig.getPort());
	        }
	    }

	    private int parseParity(String parity) {
	        switch (parity) {
	            case "1": return SerialPort.ODD_PARITY;
	            case "2": return SerialPort.EVEN_PARITY;
	            case "3": return SerialPort.MARK_PARITY;
	            case "4": return SerialPort.SPACE_PARITY;
	            default:  return SerialPort.NO_PARITY;
	        }
	    }

	    private int parseStopBits(String stopBits) {
	    	 return switch (stopBits.trim().toUpperCase()) {
	         case "NONE" -> SerialPort.ONE_STOP_BIT; 
	         case "1"    -> SerialPort.ONE_STOP_BIT;
	         case "2"    -> SerialPort.TWO_STOP_BITS;
	         default     -> SerialPort.ONE_STOP_BIT; 
	         };
	    }
	}
