package com.hext;

import com.hext.config.Config;
import com.hext.modules.BaseModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class HextClient implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "hext";
    public static MinecraftClient mc;
    public static List<BaseModule> modules = new ArrayList<>();

    @Override
    public void onInitialize() {}

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
        Config.load();
        ModuleManager.getInstance().init();
    }
}
