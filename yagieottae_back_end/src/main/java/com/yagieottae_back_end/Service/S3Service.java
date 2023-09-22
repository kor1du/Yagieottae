package com.yagieottae_back_end.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service
{
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public ResponseDto uploadFile(MultipartFile multipartFile)
    {
        ResponseDto returnValue;

        try
        {
            String fileName = multipartFile.getOriginalFilename();

            //파일 형식 구하기
            assert fileName != null;
            String extension = fileName.split("\\.")[1];
            String contentType;

            //파일 형식 Switch문
            switch (extension)
            {
                case "jpeg", "jpg" -> contentType = "image/jpeg";
                case "png" -> contentType = "image/png";
                case "txt" -> contentType = "text/plain";
                case "csv" -> contentType = "text/csv";
                default ->
                        throw new CustomBadRequestException("지원하지 않는 파일 형식입니다. jpg, jpeg, png, txt, csv 파일만 지원가능합니다.");
            }

            try
            {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);

                amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));
            } catch (Exception e)
            {
                throw new CustomBadRequestException("파일 저장에 실패하였습니다.");
            }

            returnValue = ResponseDto.builder()
                    .message("파일 저장에 성공하였습니다.")
                    .body(JsonUtil.ObjectToJsonObject("fileUrl", amazonS3.getUrl(bucket, fileName).toString()))
                    .build();
        } catch (CustomBadRequestException e)
        {
            throw e;
        } catch (Exception e)
        {
            log.error("uploadFile Unhandled Exception occured", e);
            throw new CustomBadRequestException("파일 업로드중 오류가 발생하였습니다!");
        }

        return returnValue;
    }
}
