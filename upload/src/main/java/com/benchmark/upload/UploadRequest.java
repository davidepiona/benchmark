package com.benchmark.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;

@Data
@NoArgsConstructor
public class UploadRequest {
    private String id;

    private String fileContent;

    private int partId;

    private int partCount;

    public UploadRequest(String id, String fileContent, int partId, int partCount) {
        this.id = id;
        this.fileContent = fileContent;
        this.partId = partId;
        this.partCount = partCount;
    }
}
