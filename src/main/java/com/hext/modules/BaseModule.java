package com.hext.modules;

import net.minecraft.client.option.KeyBinding;
import com.hext.modules.ModuleSetting;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseModule {
    public String name;
    public KeyBinding keyBinding;
    public boolean enabled = false;
    private List<ModuleSetting> settings = new ArrayList<>();

    public BaseModule(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Module name boş olamaz");
        }
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

    public List<ModuleSetting> getSettings() {
        return settings;
    }

    public void addSetting(ModuleSetting setting) {
        if (setting == null) {
            throw new IllegalArgumentException("Setting null olamaz");
        }
        settings.add(setting);
    }

    public void addBoolean(String name, boolean defaultValue) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Setting adı boş olamaz");
        }
        settings.add(new ModuleSetting(name, ModuleSetting.Type.BOOLEAN, defaultValue));
    }

    public void addSlider(String name, double defaultValue, double min, double max) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Setting adı boş olamaz");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min değeri max'tan büyük olamaz");
        }
        settings.add(new ModuleSetting(name, ModuleSetting.Type.SLIDER, defaultValue, min, max));
    }

    public void addText(String name, String defaultValue) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Setting adı boş olamaz");
        }
        settings.add(new ModuleSetting(name, ModuleSetting.Type.TEXT, defaultValue));
    }

    public <T> T getSetting(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (ModuleSetting s : settings) {
            if (s != null && s.name != null && s.name.equals(name)) {
                return (T) s.value;
            }
        }
        return null;
    }

    public ModuleSetting getSettingObj(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (ModuleSetting s : settings) {
            if (s != null && s.name != null && s.name.equals(name)) {
                return s;
            }
        }
        return null;
    }
}
