package com.codeit.sb01_deokhugam.domain.notification.repository;

import java.util.List;

import com.codeit.sb01_deokhugam.domain.notification.dto.request.NotificationSearchCondition;
import com.codeit.sb01_deokhugam.domain.notification.entity.Notification;

public interface CursorNotificationRepository {

	List<Notification> findByCursorPagination(NotificationSearchCondition condition, int limit);

}
