package com.hext.client.module.impl.misc;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.SliderSetting;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {

    private final SliderSetting brightness = addSetting(new SliderSetting(
            "Brightness", "Parlaklık seviyesi", 10.0, 1.0, 10.0, 0.5
    ));

    private double oldGamma = 1.0;

    public Fullbright() {
        super("Fullbright", "Gamma değerini artırarak karanlığı kaldırır", Category.MISC);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            oldGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(brightness.getValue());
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(oldGamma);
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        if (client.options != null) {
            client.options.getGamma().setValue(brightness.getValue());
        }
    }
}
