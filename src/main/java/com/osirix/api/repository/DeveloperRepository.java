package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
