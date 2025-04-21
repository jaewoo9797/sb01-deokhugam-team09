package com.codeit.sb01_deokhugam.domain.book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
	boolean existsByIsbn(String isbn);
}
