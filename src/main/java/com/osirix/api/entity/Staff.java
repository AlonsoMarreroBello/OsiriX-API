package com.osirix.api.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("STAFF")
public class Staff extends User {

    @OneToMany(mappedBy = "assignedAdmin")
    private List<Publisher> assignedPublishers;  
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "staff_roles",
        joinColumns = @JoinColumn(name = "staff_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

}
