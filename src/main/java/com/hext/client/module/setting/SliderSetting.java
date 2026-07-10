package com.hext.client.module.setting;

public class SliderSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public SliderSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }

    public void setValueClamped(double v) {
        value = Math.max(min, Math.min(max, Math.round(v / step) * step));
    }

    public float getPercentage() { return (float) ((value - min) / (max - min)); }

    @Override
    public String getDisplayValue() {
        if (value == Math.floor(value)) return String.valueOf(value.intValue());
        return String.format("%.1f", value);
    }
}
