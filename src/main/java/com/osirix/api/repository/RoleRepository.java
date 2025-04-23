package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
