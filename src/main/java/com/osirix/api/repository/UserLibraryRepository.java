package com.osirix.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.UserLibrary;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

	List<UserLibrary> findByUserId(Long userId);
}
