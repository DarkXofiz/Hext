package com.example.cheat;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {
    public ModMenuScreen() {
        super(Text.literal("Hext Client Menu"));
    }

    @Override
    protected void init() {
        int y = 50;
        addDrawableChild(ButtonWidget.builder(Text.literal("Killaura: " + (CheatModClient.killaura ? "ON" : "OFF")), b -> CheatModClient.killaura = !CheatModClient.killaura).dimensions(50, y, 200, 20).build());
        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("Speed: " + (CheatModClient.speed ? "ON" : "OFF")), b -> CheatModClient.speed = !CheatModClient.speed).dimensions(50, y, 200, 20).build());
        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("ESP: " + (CheatModClient.esp ? "ON" : "OFF")), b -> CheatModClient.esp = !CheatModClient.esp).dimensions(50, y, 200, 20).build());
        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("Hitbox: " + (CheatModClient.hitbox ? "ON" : "OFF")), b -> CheatModClient.hitbox = !CheatModClient.hitbox).dimensions(50, y, 200, 20).build());
        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("SpeedMine: " + (CheatModClient.speedmine ? "ON" : "OFF")), b -> CheatModClient.speedmine = !CheatModClient.speedmine).dimensions(50, y, 200, 20).build());
        y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("XRay: " + (CheatModClient.xray ? "ON" : "OFF")), b -> CheatModClient.xray = !CheatModClient.xray).dimensions(50, y, 200, 20).build());
    }
}
