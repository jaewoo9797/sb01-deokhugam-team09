package com.codeit.sb01_deokhugam.domain.book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID>, BookRepositoryCustom {
	//JpaRepository 기본 제공 메서드
	boolean existsByIsbn(String isbn);

}
