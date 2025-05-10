package com.codeit.sb01_deokhugam.domain.review.repository;

import static com.codeit.sb01_deokhugam.domain.book.entity.QBook.*;
import static com.codeit.sb01_deokhugam.domain.review.entity.QReview.*;
import static com.codeit.sb01_deokhugam.domain.user.entity.QUser.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Review> findByIdNotDeleted(UUID id) {
		Review entity = queryFactory
			.selectFrom(review)
			.where(review.id.eq(id)
				.and(review.deleted.isFalse()))
			.fetchOne();
		return Optional.ofNullable(entity);
	}

	@Override
	public List<Review> findListByCursor(
		UUID filterUserId,
		UUID filterBookId,
		String keyword,
		Instant after,
		String cursor,
		String orderBy,
		String direction,
		int limit
	) {
		BooleanBuilder builder = new BooleanBuilder();

		if (filterUserId != null) {
			builder.and(review.author.id.eq(filterUserId));
		}
		if (filterBookId != null) {
			builder.and(review.book.id.eq(filterBookId));
		}
		if (keyword != null && !keyword.isBlank()) {
			builder.and(
				review.content.containsIgnoreCase(keyword)
					.or(review.author.nickname.containsIgnoreCase(keyword)
						.or(review.book.title.containsIgnoreCase(keyword)))
			);
		}
		builder.and(review.deleted.isFalse());

		// 정렬 필드 지정
		//PathBuilder<?> entityPath = new PathBuilder<>(Review.class, "review");
		OrderSpecifier<?> primary;
		OrderSpecifier<?> secondary = review.createdAt.desc();

		boolean asc = "ASC".equalsIgnoreCase(direction);
		if ("rating".equalsIgnoreCase(orderBy)) {
			primary = asc ? review.rating.asc() : review.rating.desc();
		} else { // createdAt 기본
			primary = asc ? review.createdAt.asc() : review.createdAt.desc();
		}

		// 커서 조건
		if (cursor != null && after != null) {
			BooleanBuilder cursorPred = new BooleanBuilder();
			if ("rating".equalsIgnoreCase(orderBy)) {
				BigDecimal ratingCursor = new BigDecimal(cursor);
				if (asc) {
					cursorPred.or(review.rating.gt(ratingCursor));
					cursorPred.or(review.rating.eq(ratingCursor)
						.and(review.createdAt.gt(after)));
				} else {
					cursorPred.or(review.rating.lt(ratingCursor));
					cursorPred.or(review.rating.eq(ratingCursor)
						.and(review.createdAt.lt(after)));
				}
			} else {
				// createdAt cursor
				Instant createdCursor = Instant.parse(cursor);
				if (asc) {
					cursorPred.or(review.createdAt.gt(createdCursor));
					cursorPred.or(review.createdAt.eq(createdCursor)
						.and(review.id.gt(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
				} else {
					cursorPred.or(review.createdAt.lt(createdCursor));
					cursorPred.or(review.createdAt.eq(createdCursor)
						.and(review.id.lt(UUID.fromString("00000000-0000-0000-0000-000000000000"))));
				}
			}
			builder.and(cursorPred);
		}

		return queryFactory
			.selectFrom(review)
			.join(review.author, user).fetchJoin()
			.join(review.book, book).fetchJoin()
			.where(builder)
			.orderBy(primary, secondary)
			.limit(limit)
			.fetch();
	}

	@Override
	public long countByFilter(UUID filterUserId, UUID filterBookId, String keyword) {
		BooleanBuilder builder = new BooleanBuilder();

		if (filterUserId != null) {
			builder.and(review.author.id.eq(filterUserId));
		}
		if (filterBookId != null) {
			builder.and(review.book.id.eq(filterBookId));
		}
		if (keyword != null && !keyword.isBlank()) {
			builder.and(
				review.content.containsIgnoreCase(keyword)
					.or(review.author.nickname.containsIgnoreCase(keyword))
			);
		}
		builder.and(review.deleted.isFalse());

		return queryFactory
			.selectFrom(review)
			.where(builder)
			.fetchCount();
	}
}
