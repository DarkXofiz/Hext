package com.hext.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.hext.HextClient;
import com.hext.modules.BaseModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Path CONFIG_PATH = Paths.get("config/hext_config.json");
    private static final Path CONFIG_DIR = CONFIG_PATH.getParent();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
        if (HextClient.modules == null || HextClient.modules.isEmpty()) {
            System.out.println("[HEXT] Modules listesi boş, config kaydedilemiyor!");
            return;
        }

        try {
            Map<String, Object> data = new HashMap<>();

            HextClient.modules.forEach(m -> {
                if (m == null || m.name == null) return;

                try {
                    Map<String, Object> moduleData = new HashMap<>();
                    moduleData.put("enabled", m.enabled);

                    // Settings'i kaydet
                    if (m.getSettings() != null) {
                        Map<String, Object> settings = new HashMap<>();
                        m.getSettings().forEach(s -> {
                            if (s != null && s.name != null && s.value != null) {
                                settings.put(s.name, s.value);
                            }
                        });
                        moduleData.put("settings", settings);
                    }

                    data.put(m.name, moduleData);
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül kayıt hatası (" + m.name + "): " + e.getMessage());
                }
            });

            String json = GSON.toJson(data);
            Files.writeString(CONFIG_PATH, json);
            System.out.println("[HEXT] Config başarıyla kaydedildi: " + CONFIG_PATH);

        } catch (IOException e) {
            System.err.println("[HEXT] Config dosya yazma hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[HEXT] Config kayıt genel hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            System.out.println("[HEXT] Config dosyası bulunamadı, varsayılan ayarlar kullanılıyor.");
            return;
        }

        if (HextClient.modules == null || HextClient.modules.isEmpty()) {
            System.err.println("[HEXT] Modules listesi boş, config yüklenemedi!");
            return;
        }

        try {
            String json = Files.readString(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) {
                System.out.println("[HEXT] Config dosyası boş!");
                return;
            }

            JsonObject data = GSON.fromJson(json, JsonObject.class);
            if (data == null) {
                System.err.println("[HEXT] Config JSON parse hatası!");
                return;
            }

            HextClient.modules.forEach(m -> {
                if (m == null || m.name == null) return;

                try {
                    if (data.has(m.name)) {
                        JsonObject moduleData = data.getAsJsonObject(m.name);

                        // Enabled durumunu yükle
                        if (moduleData.has("enabled")) {
                            m.enabled = moduleData.get("enabled").getAsBoolean();
                        }

                        // Settings'i yükle
                        if (moduleData.has("settings")) {
                            JsonObject settingsJson = moduleData.getAsJsonObject("settings");
                            m.getSettings().forEach(s -> {
                                if (s == null || s.name == null) return;

                                try {
                                    if (settingsJson.has(s.name)) {
                                        JsonElement element = settingsJson.get(s.name);
                                        s.value = parseValue(element, s.value);
                                    }
                                } catch (Exception e) {
                                    System.err.println("[HEXT] Setting yükleme hatası (" + s.name + "): " + e.getMessage());
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül yükleme hatası (" + m.name + "): " + e.getMessage());
                }
            });

            System.out.println("[HEXT] Config başarıyla yüklendi.");

        } catch (IOException e) {
            System.err.println("[HEXT] Config dosya okuma hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[HEXT] Config yükleme genel hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * JSON element'i type'ına göre parse eder
     */
    private static Object parseValue(JsonElement element, Object defaultValue) {
        try {
            if (element.isJsonPrimitive()) {
                if (element.getAsJsonPrimitive().isBoolean()) {
                    return element.getAsBoolean();
                } else if (element.getAsJsonPrimitive().isNumber()) {
                    Number num = element.getAsNumber();
                    if (defaultValue instanceof Double) {
                        return num.doubleValue();
                    } else if (defaultValue instanceof Integer) {
                        return num.intValue();
                    } else if (defaultValue instanceof Float) {
                        return num.floatValue();
                    }
                    return num.doubleValue();
                } else if (element.getAsJsonPrimitive().isString()) {
                    return element.getAsString();
                }
            }
        } catch (Exception e) {
            System.err.println("[HEXT] Value parse hatası: " + e.getMessage());
        }
        return defaultValue;
    }
}
