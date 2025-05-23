package com.osirix.api.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.osirix.api.entity.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	
	@Query("SELECT f FROM Friendship f WHERE f.user1.id = :userId OR f.user2.id = :userId")
	Collection<Friendship> findByUserId(Long userId);
	
	Optional<Friendship> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

}
