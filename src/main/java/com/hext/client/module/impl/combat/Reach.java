package com.hext.client.module.impl.combat;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.SliderSetting;

public class Reach extends Module {

    private final SliderSetting distance = addSetting(new SliderSetting(
            "Distance", "Saldırı menzili", 3.5, 3.0, 7.0, 0.1
    ));

    public Reach() {
        super("Reach", "Saldırı menzilini gösterir", Category.COMBAT);
    }

    public double getDistance() { return distance.getValue(); }
}
