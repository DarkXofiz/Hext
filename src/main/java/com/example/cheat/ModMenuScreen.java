package com.example.cheat;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {
    public ModMenuScreen() {
        super(Text.literal("ExClient Menu - Tüm Hileler"));
    }

    @Override
    protected void init() {
        int y = 50;

        // Killaura
        addDrawableChild(ButtonWidget.builder(Text.literal("Killaura: " + (CheatModClient.killaura ? "ON" : "OFF")), button -> {
            CheatModClient.killaura = !CheatModClient.killaura;
            button.setMessage(Text.literal("Killaura: " + (CheatModClient.killaura ? "ON" : "OFF")));
        }).dimensions(50, y, 200, 20).build());
        y += 30;

        // Killaura Range Slider
        addDrawableChild(new SliderWidget(50, y, 200, 20, Text.literal("Range: " + CheatModClient.killauraRange), (CheatModClient.killauraRange - 3) / 5) {
            @Override
            protected void updateMessage() {
                CheatModClient.killauraRange = 3 + value * 5;
                setMessage(Text.literal("Killaura Range: " + String.format("%.1f", CheatModClient.killauraRange)));
            }
        });
        y += 40;

        // Speed
        addDrawableChild(ButtonWidget.builder(Text.literal("Speed: " + (CheatModClient.speed ? "ON" : "OFF")), button -> {
            CheatModClient.speed = !CheatModClient.speed;
            button.setMessage(Text.literal("Speed: " + (CheatModClient.speed ? "ON" : "OFF")));
        }).dimensions(50, y, 200, 20).build());
        y += 30;

        // Speed Multi Slider
        addDrawableChild(new SliderWidget(50, y, 200, 20, Text.literal("Speed: " + CheatModClient.speedMultiplier), (CheatModClient.speedMultiplier - 1) / 2) {
            @Override
            protected void updateMessage() {
                CheatModClient.speedMultiplier = 1 + value * 2;
                setMessage(Text.literal("Speed x" + String.format("%.1f", CheatModClient.speedMultiplier)));
            }
        });
        y += 40;

        // ESP, Hitbox, XRay, Speedmine benzer buton + slider ekle (aynı şekilde)

        // Örnek ESP
        addDrawableChild(ButtonWidget.builder(Text.literal("ESP: " + (CheatModClient.esp ? "ON" : "OFF")), button -> {
            CheatModClient.esp = !CheatModClient.esp;
            button.setMessage(Text.literal("ESP: " + (CheatModClient.esp ? "ON" : "OFF")));
        }).dimensions(50, y, 200, 20).build());
        y += 30;

        // Hitbox Scale
        addDrawableChild(new SliderWidget(50, y, 200, 20, Text.literal("Hitbox Scale"), CheatModClient.hitboxScale / 2) {
            @Override
            protected void updateMessage() {
                CheatModClient.hitboxScale = value * 2;
                setMessage(Text.literal("Hitbox Scale: " + String.format("%.1f", CheatModClient.hitboxScale)));
            }
        });

        // Diğer hileler (XRay, Speedmine) aynı mantıkla ekle
    }
}

