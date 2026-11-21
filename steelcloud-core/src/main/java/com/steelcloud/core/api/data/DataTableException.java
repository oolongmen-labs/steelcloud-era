package com.steelcloud.core.api.data;

public class DataTableException extends RuntimeException {
    public DataTableException(String message) {
        super(message);
    }

    public DataTableException(String message, Throwable cause) {
        super(message, cause);
    }
}