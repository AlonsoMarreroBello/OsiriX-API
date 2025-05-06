package com.osirix.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "request_type", discriminatorType = DiscriminatorType.STRING)
public class Request {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User que hace una solicitud

	@Column(nullable = false)
    private LocalDate requestDate;  // Fecha en que se realiza la solicitud

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus;  // Estado de la solicitud

    private String adminComments;  // Comentarios del admin
    
    private String requestTitle;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestBody;

	public void setRequestStatus(String status) {
		switch (status.toUpperCase()) {
			case "PENDANT": 
				this.requestStatus = RequestStatus.PENDANT;
			break;
			case "ACCEPTED": 
				this.requestStatus = RequestStatus.ACCEPTED;
				break;
			case "REVOKED": 
				this.requestStatus = RequestStatus.REVOKED;
				break;
		
			default:
				throw new IllegalArgumentException("Unexpected value: " + status);
		}
	}
	
	public void setRequestStatus(RequestStatus status) {
		this.requestStatus = status;
	}
}
