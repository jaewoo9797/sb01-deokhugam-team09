package com.codeit.sb01_deokhugam.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sourceforge.tess4j.Tesseract;

@Configuration
public class OcrConfig {

	//OCR 오픈소스 Tesseract 관련 config 파일입니다.
	//tessdata폴더에는 언어 데이터 파일이 들어있습니다.
	@Bean
	public Tesseract tesseract() {
		Tesseract tesseract = new Tesseract();
		String tessdataPath = Paths.get("src", "main", "resources", "ocr", "tessdata").toString(); //tessdata 경로설정
		tesseract.setDatapath(tessdataPath);// tessdata 폴더 설정
		tesseract.setLanguage("kor+eng");  //
		return tesseract;
	}
}
