package com.hext;

import com.hext.modules.BaseModule;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class HextClient implements ClientModInitializer {
    public static final String MOD_ID = "hext";
    public static MinecraftClient mc;
    public static List<BaseModule> modules = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
        ModuleManager.getInstance().init();
        com.hext.config.Config.load();
    }
}
