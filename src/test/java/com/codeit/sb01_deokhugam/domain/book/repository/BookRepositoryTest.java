package com.codeit.sb01_deokhugam.domain.book.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
import com.codeit.sb01_deokhugam.domain.book.entity.Book;

@DataJpaTest
@ActiveProfiles("test") //인메모리 DB사용
@Import({QueryDslConfig.class, JpaAuditingConfiguration.class})
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager; //test용 엔티티매니저- 엔티티를 저장 및 조회할때마다 영속성 컨텍스트에 엔티티를 보관하고 관리한다.

	@Autowired
	private BookRepository bookRepository;

	private Book book;
	private Book book2;
	private Book book3;

	private String keyword = "제목";
	private String orderBy = "title";
	private String direction = "DESC";
	private String cursor = null;
	private Instant after = null;
	private Integer limit = 10;

	@BeforeEach
	void setUp() {

		/**title, pubishedDate, reviewCount, rating으로 1차 정렬합니다.
		 * 만약 정렬기준에 대해 동일한 값을 가지고 있다면, 2차적으로 createAt으로 정렬합니다.
		 * 2차 정렬 여부를 테스트하기 위하여, book과 book2의 제목, book3의 리뷰수, 레이팅은 동일합니다.
		 */
		book = new Book(
			"제목", "저자", "책입니다.", "12345678",
			"출판사", LocalDate.parse("2025-01-01"),
			"https://test.com",
			5, new BigDecimal("3.0"), false
		);

		book2 = new Book(
			"제목2", "저자", "책입니다2.", "22345678",
			"출판사", LocalDate.parse("2025-01-02"),
			"https://test.com",
			0, new BigDecimal("0.0"), false
		);

		book3 = new Book(
			"제목2", "저자", "책입니다.", "32345678",
			"출판사", LocalDate.parse("2025-01-01"),
			"https://test.com",
			5, new BigDecimal("3.0"), false
		);

	}

	@Nested
	@DisplayName("논리적 삭제되지 않은 도서 조회")
	public class FindByIdNotLogicalDelete {

		@Test
		@DisplayName("논리적 삭제되지 않은 도서를 조회한다.")
		public void findByIdNotLogicalDelete_NotDeletedBook_ReturnsBook() {
			//given
			bookRepository.save(book);
			UUID id = book.getId(); //저장 후 id가 생김.

			// 영속성 컨텍스트 초기화 - 1차 캐시 비우기. 이미 조회되어 1차 캐시에 저장된 것을 찾지 않고, DB에서 찾게 하도록 설정한다.
			entityManager.flush();
			entityManager.clear();

			//when
			Optional<Book> result = bookRepository.findByIdNotLogicalDelete(id);

			//then
			assertThat(result).isPresent();
			assertThat(result.get().getId()).isEqualTo(id);
		}

		@Test
		@DisplayName("논리적 삭제된 도서를 조회한다. 빈 Optional을 반환받는다.")
		public void findByIdNotLogicalDelete_DeletedBook_ReturnsOptional() {
			//given
			bookRepository.save(book);
			book.softDelete(); //논리적 삭제
			UUID id = book.getId();

			entityManager.flush();
			entityManager.clear();

			//when
			Optional<Book> result = bookRepository.findByIdNotLogicalDelete(id);

			//then
			assertThat(result).isEmpty();
		}
	}

	@Nested
	@DisplayName("도서 목록을 커서 기반 페이지네이션으로 조회한다.")
	public class findListByCursor {

		@BeforeEach
		void setUp() throws InterruptedException {

			//DB 데이터 지우기
			bookRepository.deleteAll();

			//저장. createAt에 100ms 차이를 주기 위하여 sleep(100) 수행
			bookRepository.save(book);
			Thread.sleep(100);
			bookRepository.save(book2);
			Thread.sleep(100);
			bookRepository.save(book3);

			//영속성 컨텍스트 1차 캐시 지우기
			entityManager.flush();
			entityManager.clear();

		}

		@Test
		@DisplayName("검색어 필터링과 제목 정렬기준으로 도서를 커서 기반 페이징하여 처음 조회한다. 제목이 동일한 경우, 2차 커서인 createAt으로 정렬되었는지 근삿값으로 비교한다.")
		public void findListByCursor_ReturnsBook() {
			//given
			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();

			//제목 순대로 정렬되었는가
			assertThat(books.get(0).getTitle()).isEqualTo("제목2");

			//제목이 동일한 경우, 2차 커서인 createAt으로 정렬되었는지 근삿값으로 비교한다.
			/* 시간 정밀도 차이: 시간에 대해 자바(10^-9, 나노세컨드)-postgreSql(10^-6, 마이크로세컨드) 정밀도 차이로 인해 같은 엔티티의 시간 값을 다르게 표현합니다.
			  따라서 이를 해결하기 위해, 논리적으로 동일한 엔티티 book3과 books.get(0)의 creatAt의 차이가 100마이크로 초 이하인지 검증합니다
			 */
			Duration diff = Duration.between(book3.getCreatedAt(), books.get(0).getCreatedAt());
			assertThat(Math.abs(diff.toNanos())).isLessThanOrEqualTo(100_000); // 100μs

		}

		@Test
		@DisplayName("키워드 없이 reviewcount 정렬기준과 커서를 이용해 검색한다.")
		public void findListByCursor_FilterWithReviewCountCursor_ReturnsBook() {
			//given
			keyword = null;
			orderBy = "reviewCount";
			limit = 2;
			cursor = "5";
			after = book.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();

			//reviewcount 0인가
			assertThat(books.get(books.size() - 1).getReviewCount()).isEqualTo(0);

		}

		@Test
		@DisplayName("키워드 없이 title 정렬기준과 커서를 이용해 내림차순 검색한다.")
		public void findListByCursor_FilterWithTitleCursorDesc_ReturnsBook() {
			//given
			keyword = null;
			limit = 2;
			cursor = "제목2";
			after = book2.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();

			//제목이 "제목"인가
			assertThat(books.get(books.size() - 1).getTitle()).isEqualTo("제목");

		}

		@Test
		@DisplayName("키워드 없이 title 정렬기준과 커서를 이용해 오름차순 검색한다.")
		public void findListByCursor_FilterWithTitleCursor_ReturnsBook() {
			//given
			keyword = null;
			direction = "ASC";
			limit = 2;
			cursor = "제목2";
			after = book2.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();
			assertThat(books.get(books.size() - 1).getTitle()).isEqualTo("제목2");

		}

		@Test
		@DisplayName("키워드 없이 rating 정렬기준과 커서를 이용해 내림차순 검색한다.")
		public void findListByCursor_FilterWithRatingCursorDesc_ReturnsBook() {
			//given
			keyword = null;
			limit = 2;
			orderBy = "rating";
			cursor = "3.0";
			after = book.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();  // 리스트가 비어있지 않음을 확인
			assertThat(books.get(books.size() - 1).getRating()).isEqualTo("0.0");

		}

		@Test
		@DisplayName("키워드 없이 rating 정렬기준과 커서를 이용해 오름차순 검색한다.")
		public void findListByCursor_FilterWithRatingCursorAsc_ReturnsBook() {
			//given
			keyword = null;
			limit = 2;
			direction = "ASC";
			orderBy = "rating";
			cursor = "3.0";
			after = book.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();
			assertThat(books.get(books.size() - 1).getRating()).isEqualTo("3.0");

		}

		@Test
		@DisplayName("키워드 없이 publishedDate 정렬기준과 커서를 이용해 내림차순 검색한다.")
		public void findListByCursor_FilterWithLocalDateCursorDesc_ReturnsBook() {
			//given
			keyword = null;
			limit = 2;
			orderBy = "publishedDate";
			cursor = "2025-01-01";
			after = book3.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();
			assertThat(books.get(books.size() - 1).getPublishedDate()).isEqualTo("2025-01-01");

		}

		@Test
		@DisplayName("키워드 없이 publishedDate 정렬기준과 커서를 이용해 오름차순 검색한다.")
		public void findListByCursor_FilterWithLocalDateCursorAsc_ReturnsBook() {
			//given
			keyword = null;
			limit = 2;
			direction = "ASC";
			orderBy = "publishedDate";
			cursor = "2025-01-01";
			after = book3.getCreatedAt();

			//when
			List<Book> books = bookRepository.findListByCursor(keyword, after, cursor, orderBy, direction,
				limit); //service에서 limit에 1을 더해 보낸다.

			//then
			assertThat(books).isNotNull();
			assertThat(books.get(books.size() - 1).getPublishedDate()).isEqualTo("2025-01-02");

		}

	}

	@Nested
	@DisplayName("필터링된 도서 목록의 총 개수를 조회한다.")
	public class getTotalElements {

		@Test
		@DisplayName("필터링된 도서 목록의 총 개수 조회를 성공한다.")
		public void getTotalElements_returnsTotalElemets() {
			//given

			//when
			Long totalElements = bookRepository.getTotalElements(keyword);

			assertThat(totalElements).isEqualTo(3);
		}

	}

}
