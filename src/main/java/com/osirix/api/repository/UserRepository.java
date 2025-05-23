package com.osirix.api.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.class = User")
	Collection<User> findAllNormalUsers();
	
	Optional<User> findUserByUsername(String username);
	Optional<User> findUserByEmail(String email);

}
