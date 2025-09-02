package com.example.location;

import javax.persistence.*;

@Entity
@Table(name = "LI_AccessLocation")
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Location")
    private String location;

    // Getters and Setters

    public Long getId() 
    { return id; }
    public void setId(Long id) 
    { this.id = id; }

    public String getLocation() 
    { return location; }
    public void setLocation(String location) 
    { this.location = location; }
}
