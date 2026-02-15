package com.example.logstics.service;

import com.example.logstics.model.SearchRequest;
import com.example.logstics.model.SearchResponse;
import com.example.logstics.model.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAmazonS3 {
    SearchResponse search(SearchRequest searchRequest);
    UploadResponse upload(String fileName, MultipartFile file, String bucketName);
}
