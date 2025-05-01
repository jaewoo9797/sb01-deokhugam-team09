package com.codeit.sb01_deokhugam.domain.notification.dto.request;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Sort;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class NotificationSearchCondition {

	private final UUID userId;
	private final Sort.Direction direction;
	private final Instant cursor;
	private final Instant after;    // 현재 조건에 사용되지 않고 있는 필드입니다. 이 필드는 나중에 사용될 수 있습니다.

	public NotificationSearchCondition(UUID userId, Sort.Direction direction, Instant cursor, Instant after) {
		this.userId = userId;
		this.direction = direction;
		this.cursor = resolveCursor(cursor);
		this.after = resolveAfter(after);
	}

	private Instant resolveCursor(Instant instant) {
		if (instant != null) {
			return instant;
		}
		if (direction == Sort.Direction.ASC) {
			return Instant.EPOCH;
		}
		return Instant.now();
	}

	private Instant resolveAfter(Instant instant) {
		if (instant != null) {
			return instant;
		}

		return Instant.now();
	}
}

