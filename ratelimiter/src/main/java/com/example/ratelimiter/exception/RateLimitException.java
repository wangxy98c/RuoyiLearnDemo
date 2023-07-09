package com.example.ratelimiter.exception;

public class RateLimitException extends Exception{
    public RateLimitException(String message) {
        super(message);
    }
}
