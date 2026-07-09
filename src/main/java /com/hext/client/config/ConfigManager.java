package com.hext.client.config;

import com.hext.client.Hext;
import com.hext.client.module.Module;
import com.hext.client.module.setting.*;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class ConfigManager {

    private final Path configPath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve("hext.json");
    }

    public void save() {
        JsonObject root = new JsonObject();
        // Theme
        root.addProperty("theme", Hext.getInstance().getThemeManager().getCurrentTheme());

        JsonObject modules = new JsonObject();
        for (Module mod : Hext.getInstance().getModuleManager().getModules()) {
            JsonObject modObj = new JsonObject();
            modObj.addProperty("enabled", mod.isEnabled());
            modObj.addProperty("keybind", mod.getKeybind());

            JsonObject settings = new JsonObject();
            for (Setting<?> s : mod.getSettings()) {
                if (s instanceof BooleanSetting b)      settings.addProperty(s.getName(), b.getValue());
                else if (s instanceof SliderSetting sl) settings.addProperty(s.getName(), sl.getValue());
                else if (s instanceof ModeSetting m)    settings.addProperty(s.getName(), m.getValue());
                else if (s instanceof ColorSetting c)   settings.addProperty(s.getName(), c.getValue());
            }
            modObj.add("settings", settings);
            modules.add(mod.getName(), modObj);
        }
        root.add("modules", modules);

        try (Writer w = Files.newBufferedWriter(configPath)) {
            gson.toJson(root, w);
        } catch (IOException e) {
            Hext.LOGGER.error("Failed to save config", e);
        }
    }

    public void load() {
        if (!Files.exists(configPath)) return;
        try (Reader r = Files.newBufferedReader(configPath)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();

            if (root.has("theme"))
                Hext.getInstance().getThemeManager().setTheme(root.get("theme").getAsString());

            if (!root.has("modules")) return;
            JsonObject modules = root.getAsJsonObject("modules");

            for (Module mod : Hext.getInstance().getModuleManager().getModules()) {
                if (!modules.has(mod.getName())) continue;
                JsonObject modObj = modules.getAsJsonObject(mod.getName());

                boolean shouldEnable = modObj.has("enabled") && modObj.get("enabled").getAsBoolean();
                if (shouldEnable != mod.isEnabled()) mod.toggle();

                if (modObj.has("keybind"))
                    mod.setKeybind(modObj.get("keybind").getAsInt());

                if (!modObj.has("settings")) continue;
                JsonObject settings = modObj.getAsJsonObject("settings");

                for (Setting<?> s : mod.getSettings()) {
                    if (!settings.has(s.getName())) continue;
                    JsonElement el = settings.get(s.getName());
                    if (s instanceof BooleanSetting b)      b.setValue(el.getAsBoolean());
                    else if (s instanceof SliderSetting sl) sl.setValue(el.getAsDouble());
                    else if (s instanceof ModeSetting m)    m.setValue(el.getAsString());
                    else if (s instanceof ColorSetting c)   c.setValue(el.getAsInt());
                }
            }
        } catch (IOException | JsonParseException e) {
            Hext.LOGGER.error("Failed to load config", e);
        }
    }
}
