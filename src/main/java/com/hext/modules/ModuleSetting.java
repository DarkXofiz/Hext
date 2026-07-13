package com.hext.modules;

public class ModuleSetting {
    public enum Type {
        BOOLEAN,
        SLIDER,
        TEXT
    }

    public String name;
    public Type type;
    public Object value;
    public double min;
    public double max;

    public ModuleSetting(String name, Type type, Object value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Setting adı boş olamaz");
        }
        this.name = name;
        this.type = type;
        this.value = value;
        this.min = 0;
        this.max = 1;
    }

    public ModuleSetting(String name, Type type, Object value, double min, double max) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Setting adı boş olamaz");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min değeri max'tan büyük olamaz");
        }
        this.name = name;
        this.type = type;
        this.value = value;
        this.min = min;
        this.max = max;
    }
}
