package com.osirix.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.PublicationRequest;

@Repository
public interface PublicationRequestRepository extends JpaRepository<PublicationRequest, Long> {

	List<PublicationRequest> findByUserId(Long userId);
	List<PublicationRequest> findByAssignedStaffId(Long userId);
	
}
