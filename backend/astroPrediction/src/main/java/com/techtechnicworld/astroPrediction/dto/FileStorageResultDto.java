package com.techtechnicworld.astroPrediction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageResultDto {
    private String objectName;
    private String url;
    private String storedFilename;
    private String mimeType;
    private long size;
}
