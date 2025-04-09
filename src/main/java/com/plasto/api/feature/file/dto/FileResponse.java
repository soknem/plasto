package com.plasto.api.feature.file.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record FileResponse(
        String uuid,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String contentType,
        String extension,
        String uri,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long size
) {

}
