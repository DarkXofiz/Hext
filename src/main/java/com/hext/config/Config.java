package com.hext.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.hext.HextClient;
import com.hext.modules.BaseModule;
import com.hext.modules.ModuleSetting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "hext_config.json";
    private static final Path CONFIG_PATH = Paths.get(CONFIG_DIR, CONFIG_FILE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        try {
            if (HextClient.modules == null) return;

            Map<String, Object> data = new HashMap<>();

            for (BaseModule module : HextClient.modules) {
                if (module == null || module.name == null) continue;

                Map<String, Object> moduleData = new HashMap<>();
                moduleData.put("enabled", module.enabled);

                Map<String, Object> settingsMap = new HashMap<>();
                if (module.getSettings() != null) {
                    for (ModuleSetting setting : module.getSettings()) {
                        if (setting != null && setting.name != null) {
                            settingsMap.put(setting.name, setting.value);
                        }
                    }
                }
                moduleData.put("settings", settingsMap);
                data.put(module.name, moduleData);
            }

            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));

        } catch (Exception e) {
            System.err.println("[HEXT] Save error: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) return;
            if (HextClient.modules == null) return;

            String json = Files.readString(CONFIG_PATH);
            JsonObject data = GSON.fromJson(json, JsonObject.class);
            
            if (data == null) return;

            for (BaseModule module : HextClient.modules) {
                if (module == null || module.name == null) continue;
                if (!data.has(module.name)) continue;

                JsonObject moduleData = data.getAsJsonObject(module.name);

                if (moduleData.has("enabled")) {
                    module.enabled = moduleData.get("enabled").getAsBoolean();
                }

                if (moduleData.has("settings") && module.getSettings() != null) {
                    JsonObject settingsJson = moduleData.getAsJsonObject("settings");
                    
                    for (ModuleSetting setting : module.getSettings()) {
                        if (setting == null || setting.name == null) continue;
                        if (settingsJson.has(setting.name)) {
                            JsonElement element = settingsJson.get(setting.name);
                            if (element.isJsonPrimitive()) {
                                if (element.getAsJsonPrimitive().isBoolean()) {
                                    setting.value = element.getAsBoolean();
                                } else if (element.getAsJsonPrimitive().isNumber()) {
                                    setting.value = element.getAsDouble();
                                } else {
                                    setting.value = element.getAsString();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[HEXT] Load error: " + e.getMessage());
        }
    }
}
