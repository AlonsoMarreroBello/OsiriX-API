package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.App;

public interface AppRepository extends JpaRepository<App, Long> {

}
