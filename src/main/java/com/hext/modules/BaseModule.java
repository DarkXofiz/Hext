package com.hext.modules;

import net.minecraft.client.option.KeyBinding;

public abstract class BaseModule {
    public String name;
    public KeyBinding keyBinding;
    public boolean enabled = false;

    public BaseModule(String name) {
        this.name = name;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
