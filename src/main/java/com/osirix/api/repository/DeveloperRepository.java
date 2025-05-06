package com.osirix.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

	Optional<Developer> findByName(String name);
	
}
