package com.hext.modules;

import com.hext.HextClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class BaseModule {
    public String name;
    public KeyBinding keyBinding;
    public boolean enabled = false;

    public BaseModule(String name) {
        this.name = name;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
        com.hext.config.Config.save();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
