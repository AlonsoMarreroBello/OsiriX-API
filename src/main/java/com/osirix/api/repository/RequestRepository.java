package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

}
