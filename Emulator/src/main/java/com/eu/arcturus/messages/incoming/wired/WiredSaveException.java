package com.eu.arcturus.messages.incoming.wired;

public class WiredSaveException extends Exception {

    private final String message;

    public WiredSaveException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
