package com.osirix.api.entity;


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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DiscriminatorValue("PUBLICATION_REQUEST")
public class PublicationRequest extends Request {

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private Developer developer;  // Developer que desarrolló la app

    @ManyToOne
    @JoinColumn(name = "assigned_admin_id", nullable = true)
    private Staff assignedStaff;  // Admin asignado para revisar la solicitud

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    private App app;  // App solicitada para publicación

    
}
