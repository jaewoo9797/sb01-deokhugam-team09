package com.codeit.sb01_deokhugam.global.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import com.codeit.sb01_deokhugam.global.s3.exception.S3UploadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private static final String FILE_NAME_DELIMITER = "_";
	private static final String PATH_DELIMITER = "/";
	private static final String S3_PROTOCOL = "https";
	private static final String S3_HOST_TEMPLATE = "%s.s3.%s.amazonaws.com";

	private final S3Client s3Client;
	private final String s3BucketName;
	private final String s3Region;

	public String upload(MultipartFile file, String directory) {
		String s3ObjectKey = generateS3ObjectKey(file, directory);
		String url = resolveS3ObjectUrl(s3ObjectKey);
		uploadAsync(file, s3ObjectKey);
		return url;
	}

	@Async
	@Retryable(
		retryFor = {S3UploadException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 2000, multiplier = 2),
		recover = "recoverUpload"
	)
	public void uploadAsync(MultipartFile file, String s3ObjectKey) {
		try {
			PutObjectRequest req = PutObjectRequest.builder()
				.bucket(s3BucketName)
				.key(s3ObjectKey)
				.contentType(file.getContentType())
				.build();
			s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
		} catch (IOException | SdkException ex) {
			throw new S3UploadException(ErrorCode.S3_UPLOAD_ERROR, ex);
		}
	}

	@Recover
	public void recoverUpload(S3UploadException ex, MultipartFile file, String s3ObjectKey) {
		log.error("[S3 업로드 실패] key={}, filename={}, error={}",
			s3ObjectKey,
			file.getOriginalFilename(),
			ex.getMessage(),
			ex
		);
	}

	private String generateS3ObjectKey(MultipartFile file, String directory) {
		String originalFileName = file.getOriginalFilename();
		if (originalFileName == null || originalFileName.isBlank()) {
			throw new S3UploadException(ErrorCode.FILE_NAME_MISSING);
		}
		String uniqueFileName = UUID.randomUUID() + FILE_NAME_DELIMITER + originalFileName;
		return directory + PATH_DELIMITER + uniqueFileName;
	}

	private String resolveS3ObjectUrl(String s3ObjectKey) {
		String s3Host = String.format(S3_HOST_TEMPLATE, s3BucketName, s3Region);
		return UriComponentsBuilder.newInstance()
			.scheme(S3_PROTOCOL)
			.host(s3Host)
			.path(s3ObjectKey)
			.build()
			.toUriString();
	}

}
