package com.example.login;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Login")
public class User {

	 @Id
	    @Column(name = "UserID") // maps to the actual column
	    private Long userId;

    @Column(name = "LI_User")
    private String username;

    @Column(name = "LI_Pass")
    private String password;

    // Getters and Setters
    public Long getId() { return userId; }
    public void setId(Long id) { this.userId = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}