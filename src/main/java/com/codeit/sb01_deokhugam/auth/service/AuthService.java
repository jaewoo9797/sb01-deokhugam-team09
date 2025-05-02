package com.codeit.sb01_deokhugam.auth.service;

import com.codeit.sb01_deokhugam.auth.request.UserLoginRequest;
import com.codeit.sb01_deokhugam.domain.user.dto.response.UserDto;

public interface AuthService {

	UserDto login(UserLoginRequest userLoginRequest);
}
