package com.codeit.sb01_deokhugam.global.dto.response;

import java.time.Instant;
import java.util.List;

public record PageResponse<T>(
	List<T> content,
	Instant nextAfter,
	Object nextCursor,
	int size,
	boolean hasNext,
	Long totalElements
) {

}

