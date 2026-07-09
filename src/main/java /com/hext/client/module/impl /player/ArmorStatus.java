package com.hextclient.client.module.impl.player;

import com.hextclient.client.module.Module;
import com.hextclient.client.module.category.Category;
import com.hextclient.client.module.setting.BooleanSetting;
import com.hextclient.client.module.setting.ModeSetting;

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
