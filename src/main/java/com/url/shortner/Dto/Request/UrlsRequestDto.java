package com.url.shortner.Dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UrlsRequestDto {

    @NotNull(message = "URL cannot be null")
    @URL(message = "Invalid URL format")
    private String url;

    @NotNull(message = "Check value is required")
    @Range(min = 1, max = 2, message = "Check must be either 1 or 2")
    private Integer check;
}
