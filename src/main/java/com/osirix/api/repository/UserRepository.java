package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
