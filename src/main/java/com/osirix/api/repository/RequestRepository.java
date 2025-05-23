package com.osirix.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

	
	List<Request> findAllByUserId(Long userId);
	
}
