package com.codeit.sb01_deokhugam.domain.notification.repository.custom;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.entity.QNotification;
import com.codeit.sb01_deokhugam.domain.notification.repository.CursorNotificationRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CursorNotificationRepositoryImpl implements CursorNotificationRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Notification> findByCursorPagination(NotificationSearchCondition condition, int limit) {
		QNotification notification = QNotification.notification;

		return queryFactory.select(notification)
			.from(notification)
			.join(notification.user).fetchJoin()
			.join(notification.review).fetchJoin()
			.where(
				notification.user.id.eq(condition.getUserId()),
				cursorPredicate(condition, notification)
			)
			.orderBy(orderSpecifier(condition.getDirection(), notification))
			.limit(limit + 1)
			.fetch();
	}

	private BooleanExpression cursorPredicate(NotificationSearchCondition condition, QNotification notification) {
		if (condition.getDirection() == Sort.Direction.DESC) {
			return notification.createdAt.loe(condition.getCursor());    // loe 는 less or equal
		}

		return notification.createdAt.goe(condition.getCursor());    // goe 는 greater or equal
	}

	private OrderSpecifier<Instant> orderSpecifier(Sort.Direction direction, QNotification notification) {
		if (direction == Sort.Direction.DESC) {
			return notification.createdAt.desc();
		}

		return notification.createdAt.asc();
	}
}
