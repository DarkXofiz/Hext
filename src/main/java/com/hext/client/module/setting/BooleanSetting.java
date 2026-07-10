package com.hext.client.module.setting;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    public void toggle() { value = !value; }

    @Override
    public String getDisplayValue() { return value ? "ON" : "OFF"; }
}
