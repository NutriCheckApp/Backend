package com.nutricheck.backend.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileMetadata {

    private String fileUrl;

    private String fileName;

}