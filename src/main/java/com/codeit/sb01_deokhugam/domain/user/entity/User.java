package com.codeit.sb01_deokhugam.domain.user.entity;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 20, nullable = false)
	private String password;

	@Column(length = 20, nullable = false)
	private String nickname;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	public User(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public void update(String newNickname) {
		if (newNickname != null && !newNickname.equals(this.nickname)) {
			this.nickname = newNickname;
		}
	}

	public void softDelete() {
		this.isDeleted = true;
	}
}
