package com.eu.arcturus.plugin;

import com.eu.arcturus.habbohotel.users.Habbo;
import java.util.HashMap;
import java.util.HashSet;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public abstract class HabboPlugin {
    public final HashMap<Class<? extends Event>, HashSet<Method>> registeredEvents = new HashMap<>();

    public HabboPluginConfiguration configuration;
    public URLClassLoader classLoader;
    public InputStream stream;

    public abstract void onEnable() throws Exception;

    public abstract void onDisable() throws Exception;

    public boolean isRegistered(Class<? extends Event> clazz) {
        return this.registeredEvents.containsKey(clazz);
    }

    public abstract boolean hasPermission(Habbo habbo, String key);
}
