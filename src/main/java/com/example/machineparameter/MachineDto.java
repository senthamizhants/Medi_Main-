package com.example.machineparameter;

public class MachineDto {
    private String locationName;  // was machineId
    private String machineName;   // was locationName

    public MachineDto(String locationName, String machineName) {
        this.locationName = locationName;
        this.machineName = machineName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getMachineName() {
        return machineName;
    }
}
