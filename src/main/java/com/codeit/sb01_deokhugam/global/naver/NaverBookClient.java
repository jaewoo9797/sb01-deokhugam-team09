package com.codeit.sb01_deokhugam.global.naver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.codeit.sb01_deokhugam.domain.book.dto.NaverBookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class NaverBookClient {

	public NaverBookDto search(String isbn) throws JsonProcessingException {
		//TODO: 민감정보 .env파일로 옮기기
		String clientId = "9ObJRHDKexjVfqRXCx4e"; //애플리케이션 클라이언트 아이디
		String clientSecret = "Hv4Kx6O8ol"; //애플리케이션 클라이언트 시크릿

		try {
			isbn = URLEncoder.encode(isbn, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패", e);
		}

		String apiURL = "https://openapi.naver.com/v1/search/book?query=" + isbn;    // JSON 결과

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		String responseBody = get(apiURL, requestHeaders); //헤더에 클라이언트 아이디와 클라이언트 시크릿을 더해 전송

		NaverBookDto result = parseResponse(responseBody);

		return result;
	}

	private String get(String apiUrl, Map<String, String> requestHeaders) {
		//log.debug("apiUrl = {} 주소와 연결 시도", apiUrl);
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				//log.info("api = {} 가 정상 호출 되었습니다.", apiUrl);
				return readBody(con.getInputStream());
			} else { // 오류 발생
				//log.error("api = {} 오류 발생", apiUrl);
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
		}
	}

	//JSON 문자열 파싱 -> IsbnBookDto 만드는 함수
	private NaverBookDto parseResponse(String responseBody) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseBody);
		JsonNode item = root.get("items").get(0); // 첫 번째 책 정보만 가져옴

		String title = stripTags(item.get("title").asText());
		String author = item.get("author").asText();
		String description = item.get("description").asText();
		String publisher = item.get("publisher").asText();
		String pubDateStr = item.get("pubdate").asText(); // e.g. "20060320"
		String isbn = item.get("isbn").asText();
		String imageUrl = item.get("image").asText();

		// pubDate → LocalDate 변환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate publishedDate = LocalDate.parse(pubDateStr, formatter);

		// 이미지 다운로드 (byte[])
		byte[] imageBytes = downloadImage(imageUrl);
		String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

		return new NaverBookDto(title, author, description, publisher, publishedDate, isbn, imageBase64);
	}

	private byte[] downloadImage(String imageUrl) {
		try (InputStream in = new URL(imageUrl).openStream()) {
			return in.readAllBytes();
		} catch (Exception e) {
			throw new RuntimeException("이미지 다운로드 실패", e);
		}
	}

	// HTML 태그 제거 함수 (e.g. <b>논어</b> → 논어)
	private String stripTags(String text) {
		return text.replaceAll("<[^>]+>", "");
	}

}
