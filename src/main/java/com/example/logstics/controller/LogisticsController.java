package com.example.logstics.controller;

import com.example.logstics.model.*;
import com.example.logstics.service.AmazonS3Service;
import com.example.logstics.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/****
 * @Author  Rajesh Keshoju
 * @Date    2026-02-11
 */

@RestController
public class LogisticsController {

    private final AmazonS3Service amazonS3Service;
    private final RouterService routerService;

    @Autowired
    public LogisticsController(AmazonS3Service amazonS3Service, RouterService routerService) {
        this.amazonS3Service = amazonS3Service;
        this.routerService = routerService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<String> init() {
        return new ResponseEntity<>("This is Logistics Service", HttpStatus.OK);
    }

    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<String> checkHealth() {
        return new ResponseEntity<>("Service is up", HttpStatus.OK);
    }

    @PostMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SearchResponse> search(@RequestBody SearchRequest searchRequest) {
        SearchResponse searchResponse = amazonS3Service.search(searchRequest);
        searchResponse.setUserName(searchRequest.getUserName());
        searchResponse.setFileName(searchRequest.getFileName());
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<UploadResponse> upload(@RequestParam String fileName, @RequestParam("file") MultipartFile file, @RequestParam String path) {
        return new ResponseEntity<>(amazonS3Service.upload(fileName, file, path), HttpStatus.OK);
    }

    @PostMapping(value = "/api/v1/toll-plazas", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TollPlazaResponse> getTollPlazas(@RequestBody Journey journey) {
        return new ResponseEntity<>(routerService.getTollPlazasInRoute(journey), HttpStatus.OK);
    }
}
