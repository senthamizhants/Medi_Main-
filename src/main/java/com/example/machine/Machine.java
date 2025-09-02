package com.example.machine;

import javax.persistence.*;

@Entity
@Table(name = "LI_MachineMaster")  // table name
public class Machine {

    @Id
    @Column(name = "LIMM_ID")  
    private Long machineId;

    @Column(name = "LIMM_NAME")  // column name
    private String machineName;

    // Getters and Setters
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }

    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }
}
