package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BfhlResponse<T> {
    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("official_email")
    private String officialEmail;

    private T data;
}
