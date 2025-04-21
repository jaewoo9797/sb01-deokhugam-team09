package com.codeit.sb01_deokhugam.global.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@Component
public class S3StorageService {

    public String put(MultipartFile file) {

        return "link";
    }


    private byte[] decodeToBytes(String imageByte) {
        return Base64.getDecoder().decode(imageByte);
    }
}
