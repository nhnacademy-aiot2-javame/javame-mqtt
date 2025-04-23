package com.example.mqtt;

public class MessageProcessingException extends Exception {
    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
