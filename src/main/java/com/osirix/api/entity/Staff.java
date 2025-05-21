package com.osirix.api.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.osirix.api.utils.UserType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true) 
@ToString(callSuper = true) 
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("STAFF")
public class Staff extends User {

    public Staff(String username, String email, String password, LocalDate registerDate, LocalDate lastLogin, boolean isEnabled, boolean accountNotLocked,
			List<Publisher> assignedPublishers, Set<Role> roles) {
    	super.setUsername(username);
    	super.setEmail(email);
    	super.setPassword(password);
    	super.setRegisterDate(registerDate);
    	super.setLastLogin(lastLogin);
    	super.setIsEnabled(isEnabled);
    	super.setAccountNotLocked(accountNotLocked);
    	this.assignedPublishers = assignedPublishers;
    	this.roles = roles;
	}

	@OneToMany(mappedBy = "assignedAdmin")
    private List<Publisher> assignedPublishers;  
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "staff_roles",
        joinColumns = @JoinColumn(name = "staff_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

    @Override
    public UserType getType() {
    	return UserType.STAFF;
    }
    
}
