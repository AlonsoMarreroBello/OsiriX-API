package com.osirix.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osirix.api.entity.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
