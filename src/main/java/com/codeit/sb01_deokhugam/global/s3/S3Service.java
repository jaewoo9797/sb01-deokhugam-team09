package com.codeit.sb01_deokhugam.global.s3;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;
	private final String s3BucketName;

	// TODO S3 업로드 메소드 안에서 파일이름을 추출하고 저장된 File name 을 리턴하는게 나은가 방법 고민
	public void upload(MultipartFile file, String s3ObjectKey) throws IOException {
		s3Client.putObject(
			PutObjectRequest.builder()
				.bucket(s3BucketName)
				.key(s3ObjectKey)
				.contentType(file.getContentType())
				.build(),
			RequestBody.fromBytes(file.getBytes())
		);
	}
}
