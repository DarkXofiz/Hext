package com.hext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class HextClient implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "hext";

    @Override
    public void onInitialize() {
        // Fabric API init
    }

    @Override
    public void onInitializeClient() {
        ModuleManager.getInstance().init();
    }
}
