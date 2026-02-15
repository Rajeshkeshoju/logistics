package com.example.logstics.service;

import com.example.logstics.model.DownloadResponse;
import com.example.logstics.model.SearchRequest;
import com.example.logstics.model.SearchResponse;
import com.example.logstics.model.UploadResponse;
import com.example.logstics.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/****
 * @Author  Rajesh Keshoju
 * @Date    2026-02-11
 */

@Service
public class AmazonS3Service implements IAmazonS3 {

    @Value("${search.download.path: /}")
    private String searchDownloadPath;

    @Value("${search.upload.path: /}")
    private String uploadPath;

    private final S3Client s3Client;

    @Autowired
    public AmazonS3Service(@Qualifier("s3Client") S3Client s3Client) {
        this.s3Client = s3Client;
    }

    private String searchFileInS3Bucket(String folderName, String fileName)  {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(s3Client.listBuckets().buckets().get(0).name())
                .build();

        try {
            List<S3Object> s3ObjectList = s3Client.listObjectsV2(listObjectsV2Request).contents();
            for(S3Object s3Object: s3ObjectList) {
                if(folderName.equals(s3Object.key().substring(0, s3Object.key().length() - 1))) {
                    GetObjectRequest objectRequest = GetObjectRequest.builder()
                            .bucket(s3Client.listBuckets().buckets().get(0).name())
                            .key(s3Object.key() + fileName)
                            .build();
                    return new String(s3Client.getObject(objectRequest).readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "";
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        boolean status = Utils.writeToFile(
                searchFileInS3Bucket(searchRequest.getUserName(), searchRequest.getFileName()),
                searchRequest.getFileName(),
                searchDownloadPath);

        DownloadResponse downloadResponse = new DownloadResponse();
        downloadResponse.setMessage(status ? "success" : "fail");
        downloadResponse.setStatus(status? "success": "fail");
        downloadResponse.setPath(searchDownloadPath);

        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setDownloadResponse(downloadResponse);

        return searchResponse;
    }

    private CompleteMultipartUploadResponse multipartUpload(String fileName, MultipartFile multipartFile, String bucketName) {
        CreateMultipartUploadResponse multipartUploadResponse = s3Client.createMultipartUpload(request ->
                request.bucket(bucketName)
                        .key(fileName)
        );

        String uploadId = multipartUploadResponse.uploadId();
        List<CompletedPart> completedPartList = new ArrayList<>();
        int partNumber = 1;

        try (InputStream fileInputStream = multipartFile.getInputStream()) {
            byte[] buffer = new byte[5 * 1024 * 1024]; // buffer size 5MB
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                int finalPartNumber = partNumber;
                int finalBytesRead = bytesRead;
                UploadPartResponse uploadResponse = s3Client.uploadPart(request -> request.bucket(bucketName)
                                .key(fileName)
                                .uploadId(uploadId)
                                .partNumber(finalPartNumber)
                                .contentLength((long) finalBytesRead),
                        RequestBody.fromBytes(buffer)
                );

                completedPartList.add(CompletedPart.builder()
                        .partNumber(partNumber)
                        .eTag(uploadResponse.eTag())
                        .build());

                partNumber++;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error during multipart file upload: " + e.getMessage());
        }

        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(completedPartList)
                .build();
        CompleteMultipartUploadRequest completeMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .build();

        return s3Client.completeMultipartUpload(completeMultipartUploadRequest);

    }

    @Override
    public UploadResponse upload(String fileName, MultipartFile file, String path) {
        String bucketName = s3Client.listBuckets().buckets().get(0).name();
        CompleteMultipartUploadResponse multipartUploadResponse = multipartUpload(path + fileName, file, bucketName);

        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage(fileName + " uploaded with eTag " + multipartUploadResponse.eTag());
        uploadResponse.setStatus("success");
        uploadResponse.setPath(bucketName + "/" + path + fileName);

        return uploadResponse;
    }
}
