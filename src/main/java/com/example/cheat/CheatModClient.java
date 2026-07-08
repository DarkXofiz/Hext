package com.example.cheat;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {
    public ModMenuScreen() {
        super(Text.literal("§bHext Client Menu"));
    }

    @Override
    protected void init() {
        int y = 40;
        int width = 200;

        // Killaura Toggle
        addDrawableChild(ButtonWidget.builder(Text.literal("Killaura: " + (CheatModClient.killaura ? "ON" : "OFF")), btn -> {
            CheatModClient.killaura = !CheatModClient.killaura;
            btn.setMessage(Text.literal("Killaura: " + (CheatModClient.killaura ? "ON" : "OFF")));
        }).dimensions(50, y, width, 20).build());
        y += 30;

        // Killaura Range Slider
        addDrawableChild(new SliderWidget(50, y, width, 20, Text.literal("Range: " + String.format("%.1f", CheatModClient.killauraRange)), (CheatModClient.killauraRange - 3.0) / 5.0) {
            @Override
            protected void updateMessage() {
                CheatModClient.killauraRange = 3.0 + value * 5.0;
                setMessage(Text.literal("Killaura Range: " + String.format("%.1f", CheatModClient.killauraRange)));
            }

            @Override
            protected void applyValue() {
                // Değer uygulanır
            }
        });
        y += 35;

        // Speed Toggle + Slider
        addDrawableChild(ButtonWidget.builder(Text.literal("Speed: " + (CheatModClient.speed ? "ON" : "OFF")), btn -> {
            CheatModClient.speed = !CheatModClient.speed;
            btn.setMessage(Text.literal("Speed: " + (CheatModClient.speed ? "ON" : "OFF")));
        }).dimensions(50, y, width, 20).build());
        y += 30;

        addDrawableChild(new SliderWidget(50, y, width, 20, Text.literal("Speed x" + String.format("%.1f", CheatModClient.speedMultiplier)), (CheatModClient.speedMultiplier - 1.0) / 2.0) {
            @Override
            protected void updateMessage() {
                CheatModClient.speedMultiplier = 1.0 + value * 2.0;
                setMessage(Text.literal("Speed x" + String.format("%.1f", CheatModClient.speedMultiplier)));
            }

            @Override
            protected void applyValue() {}
        });
        y += 35;

        // ESP, Hitbox, XRay, Speedmine için aynı şekilde buton + slider ekleyebilirsin (istediğin zaman söyle)

        // ESP Toggle
        addDrawableChild(ButtonWidget.builder(Text.literal("ESP: " + (CheatModClient.esp ? "ON" : "OFF")), btn -> {
            CheatModClient.esp = !CheatModClient.esp;
            btn.setMessage(Text.literal("ESP: " + (CheatModClient.esp ? "ON" : "OFF")));
        }).dimensions(50, y, width, 20).build());
    }
}
