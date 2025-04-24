package com.codeit.sb01_deokhugam.domain.notification.entity;

import org.hibernate.annotations.ColumnDefault;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;
import com.codeit.sb01_deokhugam.domain.review.entity.Review;
import com.codeit.sb01_deokhugam.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseUpdatableEntity {

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_confirmed", nullable = false)
	@ColumnDefault("false")
	private boolean confirmed;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	private Notification(User user, String content, Review review) {
		this.user = user;
		this.content = content;
		this.review = review;
		this.confirmed = false;
	}

	public static Notification fromComment(User user, String content, Review review) {
		content = String.format("[%s]님이 나의 리뷰에 댓글을 남겼습니다.\n%s", user.getNickname(), review.getContent());
		return new Notification(user, content, review);
	}

	public static Notification fromLike(User user, Review review) {
		String content = String.format("[%s]님이 좋아요를 눌렀습니다.", user.getNickname());
		return new Notification(user, content, review);
	}

	public static Notification fromDailyRanking(User user, Review review, int rank) {
		String content = String.format("나의 리뷰가 일간 인기 리뷰 %d위에 선정되었습니다.", rank);
		return new Notification(user, content, review);
	}

	public static Notification fromWeeklyRanking(User user, Review review, int rank) {
		String content = String.format("나의 리뷰가 주간 인기 리뷰 %d위에 선정되었습니다.", rank);
		return new Notification(user, content, review);
	}

	public static Notification fromMonthlyRanking(User user, Review review, int rank) {
		String content = String.format("나의 리뷰가 월간 인기 리뷰 %d위에 선정되었습니다.", rank);
		return new Notification(user, content, review);
	}

	public static Notification fromAllTimeRanking(User user, Review review) {
		String content = "나의 리뷰가 역대 인기 리뷰에 선정되었습니다.";
		return new Notification(user, content, review);
	}

	public void markAsRead() {
		this.confirmed = true;
	}
}
