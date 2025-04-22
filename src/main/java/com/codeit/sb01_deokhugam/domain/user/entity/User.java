package com.codeit.sb01_deokhugam.domain.user.entity;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 20, nullable = false)
	private String password;

	@Column(length = 20, nullable = false, unique = true)
	private String nickname;

	@Setter(AccessLevel.PROTECTED)
	private Boolean isDeleted = false;
	
	public User(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}


	public void update(String newEmail, String newNickname, String newPassword) {
		if (newEmail != null && !newEmail.equals(this.email)) {
			this.email = newEmail;
		}
		if (newNickname != null && !newNickname.equals(this.nickname)) {
			this.nickname = newNickname;
		}
		if (newPassword != null && !newPassword.equals(this.password)) {
			this.password = newPassword;
		}
	}
}
