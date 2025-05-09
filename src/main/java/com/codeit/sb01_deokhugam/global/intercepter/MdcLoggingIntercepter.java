package com.codeit.sb01_deokhugam.global.intercepter;

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MdcLoggingIntercepter implements HandlerInterceptor {
	//인터셉터는 컨트롤러 호출 전과 후에 요청과 응답을 참조하거나 가공할 수 있다.

	//컨트롤러 호출전
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// 요청 id와 IP, uri 추출
		String requestId = request.getHeader("Deokhugam-request-user-id");  // 요청 헤더에서 가져오기
		String clientIp = getClientIp(request);
		String requestUri = request.getRequestURI();

		// MDC에 추가
		MDC.put("requestId", requestId);
		MDC.put("clientIp", clientIp);
		MDC.put("requestUri", requestUri);

		// 응답 헤더에 추가
		response.setHeader("Deokhugam-request-user-id", requestId);
		response.setHeader("Deokhugam-request-client-IP", clientIp);

		// 로그 출력
		log.info("");

		// true로 인터셉터 실행
		return true;
	}

	//컨트롤러 호출 후
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) {
		MDC.clear(); // clear하여 메모리 누수 방지
	}

	private String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		// 실제클라이언트 IP추출한다.
		// OSI 계층에 따라 IP가 변조된 경우에서도 정확한 IP를 추출한다.
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

}