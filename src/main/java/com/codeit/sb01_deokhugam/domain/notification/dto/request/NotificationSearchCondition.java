package com.codeit.sb01_deokhugam.domain.notification.dto.request;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Sort;

public record NotificationSearchCondition(
	UUID userId,
	Sort.Direction direction,
	Instant cursor,
	Instant after
) {
	public NotificationSearchCondition {
		cursor = resolveInstantOrNow(cursor);
		after = resolveInstantOrNow(after);
	}

	private Instant resolveInstantOrNow(Instant instant) {
		if (instant == null) {
			return Instant.now();
		}
		return instant;
	}
}

