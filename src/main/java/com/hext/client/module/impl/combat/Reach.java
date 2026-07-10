package com.hext.client.module.impl.combat;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.BooleanSetting;
import com.hext.client.module.setting.SliderSetting;

public class Reach extends Module {

    private final SliderSetting distance = addSetting(new SliderSetting(
            "Distance", "Erişim mesafesi (blok)", 3.0, 3.0, 6.0, 0.1
    ));

    private final BooleanSetting showIndicator = addSetting(new BooleanSetting(
            "Show Indicator", "HUD'da mesafeyi göster", true
    ));

    public Reach() {
        super("Reach", "Saldırı menzilini HUD'da gösterir", Category.COMBAT);
    }

    public double getDistance() { return distance.getValue(); }
    public boolean shouldShowIndicator() { return showIndicator.getValue(); }
}
