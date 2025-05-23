package com.osirix.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.UserLibrary;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

	List<UserLibrary> findByUserId(Long userId);
	
	@Query("SELECT l FROM UserLibrary l WHERE l.user.id = :userId AND l.app.appId = :appId")
	Optional<UserLibrary> findByUserIdAndAppId(Long userId, Long appId);
}
