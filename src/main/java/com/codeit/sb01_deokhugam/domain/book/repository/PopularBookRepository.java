package com.codeit.sb01_deokhugam.domain.book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;

public interface PopularBookRepository extends JpaRepository<BookRanking, UUID>, PopularBookCustomRepository {
}
