package com.yagieottae_back_end.Controller;

import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileController
{
    private final S3Service s3Service;

    @PostMapping("/")
    public ResponseEntity<ResponseDto> uploadFile(@RequestPart("file") MultipartFile file)
    {
        return ResponseEntity.ok(s3Service.uploadFile(file));
    }
}
