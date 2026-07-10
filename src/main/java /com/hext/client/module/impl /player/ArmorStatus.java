package com.hext.client.module.impl.player;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.BooleanSetting;
import com.hext.client.module.setting.ModeSetting;

public class ArmorStatus extends Module {

    private final ModeSetting position = addSetting(new ModeSetting(
            "Position", "HUD konumu", "Bottom-Right",
            "Top-Left", "Top-Right", "Bottom-Left", "Bottom-Right"
    ));

    private final BooleanSetting showDurability = addSetting(new BooleanSetting(
            "Show Durability", "Zırh dayanıklılığını göster", true
    ));

    public ArmorStatus() {
        super("ArmorStatus", "Ekipman durumunu HUD'da gösterir", Category.PLAYER);
    }

    public String getPosition() { return position.getValue(); }
    public boolean shouldShowDurability() { return showDurability.getValue(); }
}
