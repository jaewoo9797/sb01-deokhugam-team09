package com.codeit.sb01_deokhugam.domain.notification.repository.custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;
import com.codeit.sb01_deokhugam.domain.notification.repository.CursorNotificationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CursorNotificationRepositoryImpl implements CursorNotificationRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Notification> findByCursorPagination(NotificationSearchCondition condition, int limit) {
		return List.of();
	}
}
