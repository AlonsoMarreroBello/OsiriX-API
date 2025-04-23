package com.osirix.api.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apps")
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher; 

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private Developer developer; 

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDate publicationDate;

    private LocalDate editDate; 
    
    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private Integer downloads = 0;
    
    @Column(nullable = false)
    private Boolean isPublished = false;  
    
    @Column(nullable = false)
    private Boolean isVisible = false;  

    @Column(nullable = false)
    private Boolean isDownloadable = false;
    
    @ManyToMany
    @JoinTable(
        name = "app_categories", 
        joinColumns = @JoinColumn(name = "app_id"),  
        inverseJoinColumns = @JoinColumn(name = "category_id") 
    )
    private Set<Category> categories = new HashSet<>();
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    
    
}
