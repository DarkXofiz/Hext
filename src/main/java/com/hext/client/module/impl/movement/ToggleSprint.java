package com.hext.client.module.impl.movement;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class ToggleSprint extends Module {

    private final BooleanSetting omnidirectional = addSetting(new BooleanSetting(
            "Omnidirectional", "Tüm yönlerde sprint", false
    ));

    public ToggleSprint() {
        super("ToggleSprint", "Sprint'i otomatik olarak aktif tutar", Category.MOVEMENT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        boolean moving = client.options.forwardKey.isPressed()
                || (omnidirectional.getValue() && (
                    client.options.backKey.isPressed()
                    || client.options.leftKey.isPressed()
                    || client.options.rightKey.isPressed()
                ));
        if (moving && !client.player.isSprinting() && !client.player.isSubmergedInWater()) {
            client.player.setSprinting(true);
        }
    }
}
