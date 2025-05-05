package com.osirix.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	@Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
	Collection<Notification> findByUserId(Long userId);

}
