package com.codeit.sb01_deokhugam.domain.book.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseUpdatableEntity {

	@Column(name="title", nullable = false)
	private String title;

	@Column(name="author", nullable = false)
	private String author;

	@Column(name="isbn", nullable = false)
	private String isbn;

	@Column(name="publisher", nullable = false)
	private String publisher;

	@Column(name="published_date", nullable = false)
	private LocalDate publishedDate;

	@Column(name="thumbnail_url", nullable = false)
	private String thumbnailUrl;

	@Column(name="review_count", nullable = false)
	private Integer reviewCount;

	@Column(name="rating", nullable = false, precision = 2, scale = 1)
	private BigDecimal rating;

	@Column(name="is_deleted", nullable = false)
	private boolean isDeleted;


	public Book(String title, String author, String isbn, String publisher, LocalDate publishedDate, String thumbnailUrl, Integer reviewCount, BigDecimal rating, boolean isDeleted ) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publisher = publisher;
		this.publishedDate = publishedDate;
		this.thumbnailUrl = thumbnailUrl;
		this.reviewCount = reviewCount;
		this.rating = rating;
		this.isDeleted = isDeleted;
	}


}
