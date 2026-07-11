package com.hext.client.module.impl.combat;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class HitBox extends Module {

    private final SliderSetting size = addSetting(new SliderSetting(
            "Size", "Hitbox büyütme (genişlik)", 0.2, 0.0, 2.0, 0.05
    ));

    private final SliderSetting height = addSetting(new SliderSetting(
            "Height", "Hitbox büyütme (yükseklik)", 0.2, 0.0, 2.0, 0.05
    ));

    public HitBox() {
        super("HitBox", "Entity hitboxlarını büyütür", Category.COMBAT);
    }

    public double getSize() { return size.getValue(); }
    public double getHeight() { return height.getValue(); }
}
