package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.App;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {

}
