package com.hext.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HextScreen extends Screen {
    private static final int SETTING_H = 20;
    private static final int MARGIN = 10;
    
    // Buraya yalnızca HextScreen class'ının kodu gelecek
    // BaseModule class'ı BURADA OLMAMALI!

    public HextScreen() {
        super(Text.literal("Hext Client"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
