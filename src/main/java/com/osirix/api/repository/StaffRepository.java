package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {

}
