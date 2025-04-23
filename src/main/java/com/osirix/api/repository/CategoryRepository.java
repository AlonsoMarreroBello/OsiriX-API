package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
