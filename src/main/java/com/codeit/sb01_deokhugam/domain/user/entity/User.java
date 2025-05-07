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

		if (email == null || email.length() > 100 || !email.matches(
			"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
		}
		if (password == null || password.length() < 8 || password.length() > 20 || !password.matches(
			"^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")) {
			throw new IllegalArgumentException("비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자를 포함해야 합니다.");
		}
		if (nickname == null || nickname.length() < 2 || nickname.length() > 20) {
			throw new IllegalArgumentException("닉네임은 2자 이상 20자 이하로 입력해주세요.");
		}

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

