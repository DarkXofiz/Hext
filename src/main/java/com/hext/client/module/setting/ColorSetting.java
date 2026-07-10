package com.hext.client.module.setting;

public class ColorSetting extends Setting<Integer> {

    public ColorSetting(String name, String description, int defaultColor) {
        super(name, description, defaultColor);
    }

    public int getRed() { return (value >> 16) & 0xFF; }
    public int getGreen() { return (value >> 8) & 0xFF; }
    public int getBlue() { return value & 0xFF; }
    public int getAlpha() { return (value >> 24) & 0xFF; }

    @Override
    public String getDisplayValue() { return String.format("#%08X", value); }
}
