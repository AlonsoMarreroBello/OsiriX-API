package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
