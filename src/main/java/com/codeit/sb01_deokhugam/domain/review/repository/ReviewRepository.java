package com.codeit.sb01_deokhugam.domain.review.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
