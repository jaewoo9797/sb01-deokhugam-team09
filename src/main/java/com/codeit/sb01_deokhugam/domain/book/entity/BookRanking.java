package com.codeit.sb01_deokhugam.domain.book.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.base.BaseEntity;
import com.codeit.sb01_deokhugam.global.enumType.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "book_rankings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRanking extends BaseEntity {

	@Column(name = "period", nullable = false)
	@Enumerated(EnumType.STRING)
	private Period period;

	@Column(name = "rank", nullable = false)
	private Integer rank;

	@Column(name = "score", nullable = false, precision = 10, scale = 2)
	private BigDecimal score;

	@Column(name = "review_count", nullable = false)
	private Integer reviewCount;

	@Column(name = "rating", nullable = false, precision = 10, scale = 2)
	private BigDecimal rating;

	@Column(name = "thumbnail_url", nullable = false)
	private String thumbnailUrl;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "author", nullable = false)
	private String author;

	@Column(name = "book_id", nullable = false)
	private UUID bookId;

	public BookRanking(Period period, Integer rank, BigDecimal score, Integer reviewCount, BigDecimal rating,
		String thumbnailUrl, String title, String author, UUID bookId) {
		this.period = period;
		this.rank = rank;
		this.score = score;
		this.reviewCount = reviewCount;
		this.rating = rating;
		this.thumbnailUrl = thumbnailUrl;
		this.title = title;
		this.author = author;
		this.bookId = bookId;
	}

}
