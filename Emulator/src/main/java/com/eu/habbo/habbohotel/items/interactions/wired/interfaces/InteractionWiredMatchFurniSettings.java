package com.eu.habbo.habbohotel.items.interactions.wired.interfaces;

import com.eu.habbo.habbohotel.wired.WiredMatchFurniSetting;
import java.util.HashSet;

public interface InteractionWiredMatchFurniSettings {
    public HashSet<WiredMatchFurniSetting> getMatchFurniSettings();
    public boolean shouldMatchState();
    public boolean shouldMatchRotation();
    public boolean shouldMatchPosition();
    public boolean shouldMatchAltitude();
}
