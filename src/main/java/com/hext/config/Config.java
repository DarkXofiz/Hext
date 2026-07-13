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
import java.util.List;
import java.util.Map;

public class Config {
    private static final Path CONFIG_PATH = Paths.get("config/hext_config.json");
    private static final Path CONFIG_DIR = CONFIG_PATH.getParent();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }
        } catch (IOException e) {
            System.err.println("[HEXT] Config directory oluşturulamadı: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            List<BaseModule> modules = HextClient.modules;
            if (modules == null || modules.isEmpty()) {
                return;
            }

            Map<String, Object> data = new HashMap<>();

            for (BaseModule module : modules) {
                if (module == null || module.name == null) {
                    continue;
                }

                try {
                    Map<String, Object> moduleData = new HashMap<>();
                    moduleData.put("enabled", module.enabled);

                    List<ModuleSetting> settings = module.getSettings();
                    if (settings != null && !settings.isEmpty()) {
                        Map<String, Object> settingsMap = new HashMap<>();
                        for (ModuleSetting setting : settings) {
                            if (setting != null && setting.name != null) {
                                settingsMap.put(setting.name, setting.value);
                            }
                        }
                        moduleData.put("settings", settingsMap);
                    }

                    data.put(module.name, moduleData);
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül kayıt hatası: " + module.name);
                }
            }

            Files.writeString(CONFIG_PATH, GSON.toJson(data));
            System.out.println("[HEXT] Config kaydedildi!");

        } catch (IOException e) {
            System.err.println("[HEXT] Config yazma hatası: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                return;
            }

            List<BaseModule> modules = HextClient.modules;
            if (modules == null || modules.isEmpty()) {
                return;
            }

            String json = Files.readString(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) {
                return;
            }

            JsonObject data = GSON.fromJson(json, JsonObject.class);
            if (data == null) {
                return;
            }

            for (BaseModule module : modules) {
                if (module == null || module.name == null) {
                    continue;
                }

                if (!data.has(module.name)) {
                    continue;
                }

                try {
                    JsonObject moduleData = data.getAsJsonObject(module.name);
                    if (moduleData == null) {
                        continue;
                    }

                    if (moduleData.has("enabled")) {
                        module.enabled = moduleData.get("enabled").getAsBoolean();
                    }

                    if (moduleData.has("settings")) {
                        JsonObject settingsJson = moduleData.getAsJsonObject("settings");
                        List<ModuleSetting> settings = module.getSettings();

                        if (settings != null && !settings.isEmpty()) {
                            for (ModuleSetting setting : settings) {
                                if (setting == null || setting.name == null) {
                                    continue;
                                }

                                if (settingsJson.has(setting.name)) {
                                    JsonElement element = settingsJson.get(setting.name);
                                    setting.value = parseJsonValue(element);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül yükleme hatası: " + module.name);
                }
            }

            System.out.println("[HEXT] Config yüklendi!");

        } catch (IOException e) {
            System.err.println("[HEXT] Config okuma hatası: " + e.getMessage());
        }
    }

    private static Object parseJsonValue(JsonElement element) {
        if (element == null) {
            return null;
        }

        try {
            if (element.isJsonPrimitive()) {
                if (element.getAsJsonPrimitive().isBoolean()) {
                    return element.getAsBoolean();
                } else if (element.getAsJsonPrimitive().isNumber()) {
                    return element.getAsNumber().doubleValue();
                } else if (element.getAsJsonPrimitive().isString()) {
                    return element.getAsString();
                }
            }
        } catch (Exception e) {
            System.err.println("[HEXT] JSON parse hatası");
        }

        return null;
    }
}
