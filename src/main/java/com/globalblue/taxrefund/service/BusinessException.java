package com.globalblue.taxrefund.service;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}