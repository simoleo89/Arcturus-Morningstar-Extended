package com.eu.arcturus.habbohotel.modtool;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ModToolCategory {
    private final String name;
    private final Map<Integer, ModToolPreset> presets;

    public ModToolCategory(String name) {
        this.name = name;
        this.presets = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public void addPreset(ModToolPreset preset) {
        this.presets.put(preset.id, preset);
    }

    public Map<Integer, ModToolPreset> getPresets() {
        return this.presets;
    }

    public String getName() {
        return this.name;
    }
}
