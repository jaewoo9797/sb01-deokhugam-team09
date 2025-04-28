package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.global.dto.response.PageResponse;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.request.GetPowerUsersRequest;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.QPowerUser;
import com.codeit.sb01_deokhugam.ranking.poweruser.mapper.PowerUserMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PowerUserRepositoryImpl implements PowerUserRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final PowerUserMapper powerUserMapper;
	private final PowerUserRepository powerUserRepository;

	@Override
	public PageResponse<PowerUserDto> findPowerUsers(GetPowerUsersRequest request) {

		Sort.Direction direction = request.direction();
		int limitSize = request.limit();
		Period period = request.period();
		QPowerUser powerUser = QPowerUser.powerUser;

		BooleanBuilder builder = buildSearchCondition(request, powerUser);
		// 정렬 조건 설정
		JPAQuery<PowerUser> query = queryFactory.selectFrom(powerUser).where(builder);
		//정렬 필드, 방향 설정
		query.orderBy(getOrderSpecifier(direction, powerUser));
		// 전체 항목 수 조회
		long totalElements = powerUserRepository.countByPeriod(period);
		// 페이지 요소 수 설정. hasNext 판별하기 위해 하나 더 가져옴
		query.limit(limitSize + 1);
		// 쿼리 실행
		List<PowerUser> powerUsers = query.fetch();

		//size+1 결과에 따라 다음페이지 존재여부 설정 후, 추가로 받아온 요소 하나 삭제
		boolean hasNext = (powerUsers.size() > limitSize);
		powerUsers = hasNext ? powerUsers.subList(0, limitSize - 1) : powerUsers;
		int size = powerUsers.size();

		PowerUser lastUser = (powerUsers.isEmpty() ? null : powerUsers.get(powerUsers.size()));
		int nextCursor = (lastUser != null) ? lastUser.getRank() : 0;
		Instant nextAfter = (lastUser != null) ? lastUser.getCreatedAt() : null;
		PageResponse<PowerUserDto> powerUserDtos = powerUserMapper.toPageResponseDto(powerUsers, nextAfter, nextCursor,
			size, hasNext, totalElements);

		return powerUserDtos;
	}

	private OrderSpecifier<?>[] getOrderSpecifier(Sort.Direction direction, QPowerUser powerUser) {
		//정렬 방향 처리(기본값: ASC)
		OrderSpecifier<?> primarySortSpecifier;
		OrderSpecifier<?> secondarySortSpecifier;

		primarySortSpecifier = (direction == Sort.Direction.ASC) ? powerUser.rank.asc() : powerUser.rank.desc();
		secondarySortSpecifier =
			(direction == Sort.Direction.ASC) ? powerUser.createdAt.asc() : powerUser.createdAt.desc();

		return new OrderSpecifier[] {primarySortSpecifier, secondarySortSpecifier};
	}

	private BooleanBuilder buildSearchCondition(GetPowerUsersRequest getPowerUsersRequest, QPowerUser powerUser) {

		BooleanBuilder builder = new BooleanBuilder();

		Period period = getPowerUsersRequest.period();
		builder.and(powerUser.period.eq(period));

		//첫번째 커서(등수)가 0이 아니라면 해당 등수보다 큰 값만 반환
		//첫번째 커서가 중복불가능한 rank이며 동점이어도 같은 등수로 처리되지 않으므로
		//두번째 커서가 현재로써 쓸모 없으나, 확장가능성을 고려하여 남겨둠. 
		int cursor = getPowerUsersRequest.cursor();
		Instant after = getPowerUsersRequest.after();
		if (cursor > 0) {
			builder.and(powerUser.rank.gt(cursor));
			//.or(powerUser.rank.eq(cursor).and(powerUser.createdAt.gt(after))
		}
		return builder;
	}
}
