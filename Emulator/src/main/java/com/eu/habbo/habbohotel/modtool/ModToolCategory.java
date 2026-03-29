package com.eu.habbo.habbohotel.modtool;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class ModToolCategory {
    private final String name;
    private final Map<Integer, ModToolPreset> presets;

    public ModToolCategory(String name) {
        this.name = name;
        this.presets = Collections.synchronizedMap(new HashMap<>());
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
