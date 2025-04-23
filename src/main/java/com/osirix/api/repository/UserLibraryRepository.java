package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.UserLibrary;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

}
