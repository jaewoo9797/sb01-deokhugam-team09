package com.codeit.sb01_deokhugam.domain.book.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.codeit.sb01_deokhugam.config.JpaAuditingConfiguration;
import com.codeit.sb01_deokhugam.config.QueryDslConfig;
import com.codeit.sb01_deokhugam.domain.book.entity.BookRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;

@DataJpaTest
@ActiveProfiles("test")
@Import({QueryDslConfig.class, JpaAuditingConfiguration.class})
public class PopularBookRepositoryTest {

	@Autowired
	private PopularBookRepository popularBookRepository;

	@Autowired
	private TestEntityManager entityManager;

	private BookRanking bookRanking;
	private BookRanking bookRanking2;

	@BeforeEach
	void setUp() {

		bookRanking = new BookRanking(
			Period.DAILY,
			1,
			new BigDecimal("10.0"),
			4,
			new BigDecimal("4.0"),
			"https://test.com",
			"1등 책",
			"1등 작가",
			UUID.randomUUID()
		);
		bookRanking2 = new BookRanking(
			Period.DAILY,
			2,
			new BigDecimal("9.0"),
			3,
			new BigDecimal("3.5"),
			"https://test.com",
			"1등 책",
			"1등 작가",
			UUID.randomUUID()
		);
	}

	@Nested
	@DisplayName("인기 도서 랭킹 커서 페이지네이션")
	class findListByCursor {

		@BeforeEach
		void setUp() throws InterruptedException {
			//DB비우기
			popularBookRepository.deleteAll();

			//저장, createAt에 100ms 차이를 주기 위하여 sleep(100) 수행
			popularBookRepository.save(bookRanking);
			Thread.sleep(100);
			bookRanking2 = popularBookRepository.save(bookRanking2);

			entityManager.flush();
			entityManager.clear();
		}

		@Test
		@DisplayName("DAILY 인기 도서 랭킹을 커서 없이 처음 내림차순 조회한다.")
		public void findListByCursor_DailyNoCursorDesc_ReturnsBookRankings() {
			//given

			//when
			List<BookRanking> bookRankings = popularBookRepository.findListByCursor("DAILY", null, null, "DESC", 3);

			//then
			assertThat(bookRankings.size()).isEqualTo(2);
			assertThat(bookRankings.get(0).getRank()).isEqualTo(2);

		}

		@Test
		@DisplayName("DAILY 인기 도서 랭킹을 커서 포함 오름차순 조회한다.")
		public void findListByCursor_DailyWithCursorAsc_ReturnsBookRankings() {
			//given

			//when
			List<BookRanking> bookRankings = popularBookRepository.findListByCursor("DAILY",
				bookRanking2.getCreatedAt(), "1", "ASC", 1);

			//then
			assertThat(bookRankings).isNotNull();
			assertThat(bookRankings.get(bookRankings.size() - 1).getRank()).isEqualTo(2);
		}
	}

	@Test
	@DisplayName("인기 도서 랭킹 전체 개수 조회")
	public void getTotalElements() {
		//given
		String period = "DAILY";
		popularBookRepository.save(bookRanking);

		//when
		Long totalElements = popularBookRepository.getTotalElements(period);

		//then
		assertThat(totalElements).isEqualTo(1);

	}

}
