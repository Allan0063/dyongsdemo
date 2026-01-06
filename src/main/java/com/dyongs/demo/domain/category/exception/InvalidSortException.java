package com.dyongs.demo.domain.category.exception;

public class InvalidSortException extends RuntimeException {
    public InvalidSortException(String message) {
        super(message);
    }
}