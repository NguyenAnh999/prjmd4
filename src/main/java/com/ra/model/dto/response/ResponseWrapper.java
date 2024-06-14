package com.ra.model.dto.response;

import com.ra.constaint.EHttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseWrapper<T> {
    EHttpStatus eHttpStatus;
    int statusCode;
    String message;
    T data;
}