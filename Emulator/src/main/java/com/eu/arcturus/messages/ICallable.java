package com.eu.arcturus.messages;

import com.eu.arcturus.messages.incoming.MessageHandler;

public interface ICallable {
    void call(MessageHandler handler);
}