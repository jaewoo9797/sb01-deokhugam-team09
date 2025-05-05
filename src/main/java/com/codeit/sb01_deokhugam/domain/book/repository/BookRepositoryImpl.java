package com.codeit.sb01_deokhugam.domain.book.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
	public Optional<Book> findByIdNotLogicalDelete(UUID id) {
		QBook book = QBook.book;

		Book result = queryFactory
			.selectFrom(book)
			.where(book.id.eq(id).and(book.deleted.isFalse()))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<Book> findListByCursor(String keyword, Instant after, String cursor, String orderBy,
		String direction, Integer limit) {

		QBook book = QBook.book;
		BooleanBuilder predicate = new BooleanBuilder();

		/**
		 * 쿼리 실행순서
		 * 1. FROM- 테이블을 선택
		 * 2. WHERE- 1. 키워드 검색-부분일치를 찾는다.
		 * 			 2. 논리적 삭제가 되지 않은 것만 가져온다.
		 * 3. SELECT - 도서 row를 가져온다
		 * 4. ORDER BY - 1. 오름차순/내림차순에 따라 정렬기준, createdAt순으로 정렬한다.
		 * 				 2. 커서&after 존재시, 정렬기준에 대하여 동일한 값을 가지는 객체는 createAt으로 정렬한다.
		 * 5. 가져온 데이터를 limit 만큼 자른다.
		 */

		// 키워드 검색 -부분일치
		if (keyword != null && !keyword.isEmpty()) {
			predicate.and(
				book.title.likeIgnoreCase("%" + keyword + "%")
					.or(book.author.likeIgnoreCase("%" + keyword + "%"))
					.or(book.isbn.likeIgnoreCase("%" + keyword + "%"))
			);
		} else {
			// keyword가 null 또는 빈 문자열이면 조건 없이 전체
			predicate.and(book.isNotNull()); // 항상 true가 되는 조건
		}
		predicate.and(book.deleted.isFalse());

		// 정렬 조건
		OrderSpecifier<?> primaryOrderSpecifier; //주정렬조건
		OrderSpecifier<?> secondaryOrderSpecifier; // createdAt 기준 추가 정렬

		if ("title".equals(orderBy)) {
			primaryOrderSpecifier = direction.equalsIgnoreCase("asc") ? book.title.asc() : book.title.desc();
		} else if ("publishedDate".equals(orderBy)) {
			primaryOrderSpecifier =
				direction.equalsIgnoreCase("asc") ? book.publishedDate.asc() : book.publishedDate.desc();
		} else if ("rating".equals(orderBy)) {
			primaryOrderSpecifier = direction.equalsIgnoreCase("asc") ? book.rating.asc() : book.rating.desc();
		} else {
			primaryOrderSpecifier =
				direction.equalsIgnoreCase("asc") ? book.reviewCount.asc() : book.reviewCount.desc();
		}

		// 보조 정렬 조건으로 createdAt 추가 (항상 같은 방향으로 정렬)
		secondaryOrderSpecifier = direction.equalsIgnoreCase("asc") ? book.createdAt.asc() : book.createdAt.desc();

		// // 커서 기반 페이지네이션 조건 적용
		predicate.and(buildCursorPredicate(book, orderBy, direction, cursor, after));

		// 쿼리 실행
		List<Book> books = queryFactory.selectFrom(book)
			.where(predicate)
			.orderBy(primaryOrderSpecifier, secondaryOrderSpecifier)
			.limit(limit)
			.fetch();

		return books;
	}

	//커서 기반 페이지네이션 정렬기준, 방향, 커서,after 적용
	private BooleanBuilder buildCursorPredicate(QBook book, String orderBy, String direction, String cursor,
		Instant after) {
		BooleanBuilder cursorPredicate = new BooleanBuilder();
		if (cursor == null || after == null)
			return cursorPredicate;

		switch (orderBy) {
			case "title":
				if ("asc".equalsIgnoreCase(direction)) {
					cursorPredicate.or(book.title.gt(cursor))
						.or(book.title.eq(cursor).and(book.createdAt.gt(after)));
				} else {
					cursorPredicate.or(book.title.lt(cursor))
						.or(book.title.eq(cursor).and(book.createdAt.lt(after)));
				}
				break;
			case "publishedDate":
				LocalDate date = LocalDate.parse(cursor);
				if ("asc".equalsIgnoreCase(direction)) {
					cursorPredicate.or(book.publishedDate.gt(date))
						.or(book.publishedDate.eq(date).and(book.createdAt.gt(after)));
				} else {
					cursorPredicate.or(book.publishedDate.lt(date))
						.or(book.publishedDate.eq(date).and(book.createdAt.lt(after)));
				}
				break;
			case "rating":
				BigDecimal rating = new BigDecimal(cursor);
				if ("asc".equalsIgnoreCase(direction)) {
					cursorPredicate.or(book.rating.gt(rating))
						.or(book.rating.eq(rating).and(book.createdAt.gt(after)));
				} else {
					cursorPredicate.or(book.rating.lt(rating))
						.or(book.rating.eq(rating).and(book.createdAt.lt(after)));
				}
				break;
			case "reviewCount":
			default:
				int reviewCount = Integer.parseInt(cursor);
				if ("asc".equalsIgnoreCase(direction)) {
					cursorPredicate.or(book.reviewCount.gt(reviewCount))
						.or(book.reviewCount.eq(reviewCount).and(book.createdAt.gt(after)));
				} else {
					cursorPredicate.or(book.reviewCount.lt(reviewCount))
						.or(book.reviewCount.eq(reviewCount).and(book.createdAt.lt(after)));
				}
		}

		return cursorPredicate;
	}

	@Override
	public Long getTotalElements(String keyword) {
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
