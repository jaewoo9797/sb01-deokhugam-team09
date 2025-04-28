package com.codeit.sb01_deokhugam.domain.book.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;
import com.codeit.sb01_deokhugam.domain.book.entity.QBookRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PopularBookRepositoryImpl implements PopularBookCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<BookRanking> findListByCursor(String period, Instant after, String cursor, String direction,
		int limit) {
		QBookRanking bookRanking = QBookRanking.bookRanking;
		BooleanBuilder predicate = new BooleanBuilder();

		//검색 조건에 따른 select
		predicate.and(bookRanking.period.eq(Period.valueOf(period)));

		//정렬 조건
		OrderSpecifier<?> orderSpecifier;
		OrderSpecifier<?> secondaryOrderSpecifier;
		orderSpecifier = direction.equalsIgnoreCase("DESC") ? bookRanking.rank.desc() : bookRanking.rank.asc();
		// 보조 정렬 조건으로 createdAt 추가 (항상 같은 방향으로 정렬)
		secondaryOrderSpecifier =
			direction.equalsIgnoreCase("asc") ? bookRanking.createdAt.asc() : bookRanking.createdAt.desc();

		// 커서 기반 페이지네이션 적용.
		if (cursor != null && after != null) {
			int cursor_rank = Integer.parseInt(cursor);
			BooleanBuilder cursorCondition = new BooleanBuilder();

			if ("ASC".equalsIgnoreCase(direction)) {
				// rank가 더 큰 경우 또는 (rank가 같고 createdAt이 더 큰 경우)
				cursorCondition.or(bookRanking.rank.gt(cursor_rank));
				cursorCondition.or(bookRanking.rank.eq(cursor_rank).and(bookRanking.createdAt.gt(after)));
			} else { // DESC
				// rank가 더 작은 경우 또는 (rank가 같고 createdAt이 더 작은 경우)
				cursorCondition.or(bookRanking.rank.lt(cursor_rank));
				cursorCondition.or(bookRanking.rank.eq(cursor_rank).and(bookRanking.createdAt.lt(after)));
			}
			predicate.and(cursorCondition);
		}

		List<BookRanking> bookRankings = queryFactory.selectFrom(bookRanking)
			.where(predicate)
			.orderBy(orderSpecifier, secondaryOrderSpecifier)
			.limit(limit)
			.fetch();

		return bookRankings;

	}

	@Override
	public Long getTotalElements(String period) {
		QBookRanking bookRanking = QBookRanking.bookRanking;
		BooleanBuilder predicate = new BooleanBuilder();

		//검색 조건에 따른 select
		predicate.and(bookRanking.period.eq(Period.valueOf(period)));

		return queryFactory
			.select(bookRanking.count())
			.from(bookRanking)
			.where(predicate)
			.fetchOne();
	}
}
