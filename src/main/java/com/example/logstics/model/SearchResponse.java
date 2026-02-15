package com.example.logstics.model;

public class SearchResponse extends SearchRequest {
    private DownloadResponse downloadResponse;

    public DownloadResponse getDownloadResponse() {
        return downloadResponse;
    }

    public void setDownloadResponse(DownloadResponse downloadResponse) {
        this.downloadResponse = downloadResponse;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "downloadResponse=" + downloadResponse +
                '}';
    }
}


