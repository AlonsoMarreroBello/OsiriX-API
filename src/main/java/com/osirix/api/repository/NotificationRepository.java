package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
