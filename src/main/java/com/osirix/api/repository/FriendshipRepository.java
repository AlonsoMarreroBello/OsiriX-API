package com.osirix.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	
	@Query("SELECT f FROM Friendship f WHERE f.user1 = :userId OR f.user2 = :userId")
	Collection<Friendship> findByUserId(Long userId);

}
