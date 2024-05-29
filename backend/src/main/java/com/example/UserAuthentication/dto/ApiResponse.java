package com.example.UserAuthentication.dto;

import lombok.ToString;

/**
 * Represents a generic response structure for API interactions.
 * This class encapsulates details about the success or failure of an API operation.
 *
 * @param <T> the type of data included in the response if the operation is successful.
 */
@ToString
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    /**
     * Constructs a new ApiResponse with the specified success status, message, and data.
     *
     * @param success a boolean indicating if the request was successful.
     * @param message a string providing additional information about the request outcome.
     * @param data the data to be returned with the response, generic type.
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

