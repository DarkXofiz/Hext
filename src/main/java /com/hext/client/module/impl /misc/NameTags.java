package com.hextclient.client.module.impl.misc;

import com.hextclient.client.module.Module;
import com.hextclient.client.module.category.Category;
import com.hextclient.client.module.setting.BooleanSetting;
import com.hextclient.client.module.setting.SliderSetting;

public class NameTags extends Module {

    private final SliderSetting scale = addSetting(new SliderSetting(
            "Scale", "İsim etiketi büyüklüğü", 1.5, 0.5, 4.0, 0.1
    ));

    private final BooleanSetting showHealth = addSetting(new BooleanSetting(
            "Show Health", "Oyuncu canını göster", true
    ));

    private final BooleanSetting showDistance = addSetting(new BooleanSetting(
            "Show Distance", "Mesafeyi göster", false
    ));

    public NameTags() {
        super("NameTags", "Oyuncu isim etiketlerini büyütür ve zenginleştirir", Category.MISC);
    }

    public double getScale() { return scale.getValue(); }
    public boolean shouldShowHealth() { return showHealth.getValue(); }
    public boolean shouldShowDistance() { return showDistance.getValue(); }
}
