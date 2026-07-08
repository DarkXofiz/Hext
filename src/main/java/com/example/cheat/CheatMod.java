package com.example.cheat;

import net.fabricmc.api.ModInitializer;

public class CheatMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("MC Cheat Mod loaded! M for menu, experimental for own server.");
    }
}