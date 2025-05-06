package com.osirix.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.App;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
	
	@Query("SELECT a FROM App a WHERE a.appId > :appId ORDER BY a.appId ASC")
	List<App> findNext50(Long appId, Pageable pageable);

	Collection<App> findByNameContaining(String name);

	Collection<App> findByPublisherId(Long publisherId);

	Collection<App> findByDeveloperId(Long developerId);
	
	@Query("SELECT a FROM App a JOIN a.categories c WHERE c.id IN :categoryIds")
	List<App> findByCategoriesIds(List<Long> categoryIds);

	
}
