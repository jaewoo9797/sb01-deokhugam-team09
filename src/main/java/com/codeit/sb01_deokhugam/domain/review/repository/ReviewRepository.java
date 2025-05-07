package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	List<Review> findByCreatedAtBetween(Instant start, Instant end);
}
