package com.codeit.sb01_deokhugam.domain.notification.dto.request;

import java.time.Instant;

import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public class NotificationSearchCondition {

	private Sort.Direction direction;
	private Instant cursor;
	private Instant after;

}
