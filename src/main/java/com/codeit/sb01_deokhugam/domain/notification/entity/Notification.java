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

	public Notification(User user, String content, Review review) {
		this.user = user;
		this.content = content;
		this.review = review;
		this.confirmed = false;
	}

}
