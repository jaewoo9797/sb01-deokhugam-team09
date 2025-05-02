package com.codeit.sb01_deokhugam.global.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class PageResponse<T> {
	private final List<T> content;
	private final Instant nextAfter;
	private final Object nextCursor;
	private final int size;
	private final boolean hasNext;
	private final Long totalElements;
}
