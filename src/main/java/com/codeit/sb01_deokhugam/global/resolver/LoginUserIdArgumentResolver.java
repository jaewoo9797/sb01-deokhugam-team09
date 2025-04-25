package com.codeit.sb01_deokhugam.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codeit.sb01_deokhugam.auth.exception.LoginRequiredException;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import com.codeit.sb01_deokhugam.global.resolver.annotation.LoginUserId;

@Component
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {

	public static final String HEADER_NAME = "Deokhugam-Request-User-ID";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUserId.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String userId = webRequest.getHeader(HEADER_NAME);
		if (userId == null) {
			throw new LoginRequiredException(ErrorCode.UNAUTHORIZED);
		}
		return userId;
	}
}
