package com.codeit.sb01_deokhugam.domain.book.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
	//논리 삭제 검증
	@Query("SELECT b FROM Book b WHERE b.id = :id AND b.deleted = false")
	Optional<Book> findById(UUID id);

	boolean existsByIsbn(String isbn);
}
