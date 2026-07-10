package com.hext.client.module.impl.movement;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.ModeSetting;
import net.minecraft.client.MinecraftClient;

public class FreeLook extends Module {

    private final ModeSetting mode = addSetting(new ModeSetting(
            "Mode", "FreeLook modu", "Hold", "Hold", "Toggle"
    ));

    public float yaw = 0, pitch = 0;
    private boolean active = false;

    public FreeLook() {
        super("FreeLook", "Vücudu döndürmeden etrafı görmeye izin verir", Category.MOVEMENT);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getMode() { return mode.getValue(); }
}
