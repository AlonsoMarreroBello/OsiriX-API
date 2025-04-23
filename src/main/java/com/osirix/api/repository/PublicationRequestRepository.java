package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.PublicationRequest;

public interface PublicationRequestRepository extends JpaRepository<PublicationRequest, Long> {

}
