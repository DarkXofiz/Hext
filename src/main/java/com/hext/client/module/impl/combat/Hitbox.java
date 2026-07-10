package com.hext.client.module.impl.combat;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.SliderSetting;

public class Hitbox extends Module {

    private final SliderSetting size = addSetting(new SliderSetting(
            "Size", "Hitbox genişletme miktarı", 0.1, 0.0, 1.0, 0.01
    ));

    private final SliderSetting height = addSetting(new SliderSetting(
            "Height", "Hitbox yükseklik artışı", 0.1, 0.0, 1.0, 0.01
    ));

    public Hitbox() {
        super("Hitbox", "Entity hitbox'larını büyütür", Category.COMBAT);
    }

    public double getSize() { return size.getValue(); }
    public double getHeight() { return height.getValue(); }
}
