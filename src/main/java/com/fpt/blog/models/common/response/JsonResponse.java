package com.fpt.blog.models.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponse<T> {
    private boolean success;

    private String message;

    private T data;

    public JsonResponse(boolean isSuccess) {
        this.success = isSuccess;
    }

    public JsonResponse(boolean isSuccess, T data) {
        this.success = isSuccess;
        this.data = data;
    }
}
