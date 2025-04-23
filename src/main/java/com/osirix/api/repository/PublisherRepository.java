package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}
