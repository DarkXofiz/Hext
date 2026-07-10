package com.hext.client.module.impl.render;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.BooleanSetting;
import com.hext.client.module.setting.ModeSetting;
import net.minecraft.client.MinecraftClient;

public class Keystrokes extends Module {

    private final BooleanSetting showCPS = addSetting(new BooleanSetting(
            "Show CPS", "CPS sayacını göster", true
    ));

    private final ModeSetting style = addSetting(new ModeSetting(
            "Style", "Tuş gösterimi stili", "Modern", "Modern", "Classic"
    ));

    // CPS tracking
    private final java.util.ArrayDeque<Long> clicks = new java.util.ArrayDeque<>();

    public Keystrokes() {
        super("Keystrokes", "Basılan tuşları ve CPS'i ekranda gösterir", Category.RENDER);
    }

    public void registerClick() {
        long now = System.currentTimeMillis();
        clicks.addLast(now);
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
            clicks.pollFirst();
        }
    }

    public int getCPS() {
        long now = System.currentTimeMillis();
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
            clicks.pollFirst();
        }
        return clicks.size();
    }

    public boolean shouldShowCPS() { return showCPS.getValue(); }
    public String getStyle() { return style.getValue(); }
}
