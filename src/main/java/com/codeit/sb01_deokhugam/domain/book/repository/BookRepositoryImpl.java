package com.codeit.sb01_deokhugam.domain.book.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.book.entity.Book;
import com.codeit.sb01_deokhugam.domain.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * 논리적 삭제 되지 않은 도서를 불러옵니다. 상세조회, 수정시 사용합니다.
	 * @param id
	 * @return 도서 엔티티
	 */

	@Override
	public Optional<Book> gogo(UUID id) {
		QBook book = QBook.book;

		Book result = queryFactory
			.selectFrom(book)
			.where(book.id.eq(id).and(book.deleted.isFalse()))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<Book> goCursor(String keyword, Instant after, String cursor, String orderBy,
		String direction, Pageable pageable) {

		QBook book = QBook.book;
		BooleanBuilder predicate = new BooleanBuilder();

		// 키워드 검색 조건
		if (keyword != null && !keyword.isEmpty()) {
			predicate.and(
				book.title.likeIgnoreCase("%" + keyword + "%")
					.or(book.author.likeIgnoreCase("%" + keyword + "%"))
					.or(book.isbn.likeIgnoreCase("%" + keyword + "%"))
			);
		}

		// 커서 및 정렬 조건
		if (cursor != null) {
			if ("title".equals(orderBy)) {
				predicate.and(book.title.gt(cursor));
			} else if ("publishedDate".equals(orderBy)) {
				predicate.and(book.publishedDate.gt(LocalDate.from(Instant.parse(cursor))));
			} else if ("rating".equals(orderBy)) {
				predicate.and(book.rating.gt(Double.parseDouble(cursor)));
			} else if ("reviewCount".equals(orderBy)) {
				predicate.and(book.reviewCount.gt(Long.parseLong(cursor)));
			}
		}

		// 정렬
		OrderSpecifier<?> orderSpecifier;
		if ("title".equals(orderBy)) {
			orderSpecifier = direction.equalsIgnoreCase("asc") ? book.title.asc() : book.title.desc();
		} else if ("publishedDate".equals(orderBy)) {
			orderSpecifier = direction.equalsIgnoreCase("asc") ? book.publishedDate.asc() : book.publishedDate.desc();
		} else if ("rating".equals(orderBy)) {
			orderSpecifier = direction.equalsIgnoreCase("asc") ? book.rating.asc() : book.rating.desc();
		} else {
			orderSpecifier = direction.equalsIgnoreCase("asc") ? book.reviewCount.asc() : book.reviewCount.desc();
		}

		// 쿼리 실행
		List<Book> books = queryFactory.selectFrom(book)
			.where(predicate)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return books;
	}

	// @Query("""
	// 	SELECT b FROM Book b
	// 	WHERE (:keyword IS NULL OR b.title LIKE %:keyword%
	// 			 OR b.author LIKE %:keyword%
	// 			 OR b.isbn LIKE %:keyword%)
	// 	AND(
	// 		:cursor IS NULL OR(
	// 				(:orderBy = "title"  AND b.title > :cursor)
	// 				OR (:orderBy = "publishedDate"  AND b.publishedDate > :cursor)
	// 				OR (:orderBy = "rating"  AND b.rating > :cursor)
	// 				OR (:orderBy = "reviewCount"  AND b.reviewCount > :cursor)
	// 				)
	// 		)
	//
	// 	""")
	// List<Book> findAllByCursor(
	// 	@Param("keyword") String keyword,
	// 	@Param("after") String after,
	// 	@Param("cursor") String cursor,
	// 	@Param("orderBy") String orderBy,
	// 	@Param("direction") String direction,
	// 	Pageable pageable);

	@Override
	public Long totalElements(String keyword) {
		QBook book = QBook.book;
		BooleanBuilder predicate = new BooleanBuilder();

		if (keyword != null && !keyword.isEmpty()) {
			predicate.and(
				book.title.containsIgnoreCase(keyword)
					.or(book.author.containsIgnoreCase(keyword))
					.or(book.isbn.containsIgnoreCase(keyword))
			);
		}

		return queryFactory
			.select(book.count())
			.from(book)
			.where(predicate)
			.fetchOne();
	}

}

// ORDER BY
// CASE WHEN :sortBy = "title" AND :direction ='asc' THEN b.createdAt END ASC,
// CASE WHEN :sortBy = "title" AND :direction ='desc' THEN b.createdAt END DESC,
// CASE WHEN :sortBy = "publishedDate" AND :direction ='asc' THEN b.createdAt END ASC,
// CASE WHEN :sortBy = "publishedDate" AND :direction ='desc' THEN b.createdAt END DESC,
// CASE WHEN :sortBy = "rating" AND :direction ='asc' THEN b.createdAt END ASC,
// CASE WHEN :sortBy = "rating" AND :direction ='desc' THEN b.createdAt END DESC,
// CASE WHEN :sortBy = "reviewCount" AND :direction ='asc' THEN b.createdAt END ASC,
// CASE WHEN :sortBy = "reviewCount" AND :direction ='desc' THEN b.createdAt END DESC