package com.eu.arcturus.plugin.events.emulator;

import com.eu.arcturus.plugin.Event;

public class SSOAuthenticationEvent extends Event {
    public final String sso;

    public SSOAuthenticationEvent(String sso) {
        this.sso = sso;
    }
}