package com.osirix.api.entity;


import com.osirix.api.utils.UserType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PUBLISHER")
public class Publisher extends User {

    private String nif;
    private String publisherName;
    private String address;

    @ManyToOne
    @JoinColumn(name = "assigned_admin_id")
    private Staff assignedAdmin;  

    
    public void removeAssignedAdmin() {
    	assignedAdmin.getAssignedPublishers().remove(this);
    	this.assignedAdmin = null;
    }
    
    @Override
    public UserType getType() {
    	return UserType.PUBLISHER;
    }
    
}
