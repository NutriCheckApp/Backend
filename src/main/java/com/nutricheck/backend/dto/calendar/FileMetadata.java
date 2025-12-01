package com.nutricheck.backend.dto.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileMetadata {

    private String fileUrl;

    private String fileName;

    private Long fileSize;

}