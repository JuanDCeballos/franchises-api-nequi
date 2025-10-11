package co.juan.nequi.api.dto;

import lombok.Data;

@Data
public class ApiSuccessResponse<T> {
    private boolean success;
    private T data;

    public ApiSuccessResponse(T data) {
        this.success = true;
        this.data = data;
    }
}
