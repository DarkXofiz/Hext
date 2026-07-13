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
import java.util.List;
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
        try {
            // Modules kontrol
            List<BaseModule> modules = HextClient.modules;
            if (modules == null || modules.isEmpty()) {
                System.out.println("[HEXT] Modules listesi boş, config kaydedilemiyor!");
                return;
            }

            Map<String, Object> data = new HashMap<>();

            // Her module'ü kaydet
            for (BaseModule m : modules) {
                if (m == null || m.name == null) {
                    continue;
                }

                try {
                    Map<String, Object> moduleData = new HashMap<>();
                    moduleData.put("enabled", m.enabled);

                    // Settings'i kaydet
                    List<?> settings = m.getSettings();
                    if (settings != null && !settings.isEmpty()) {
                        Map<String, Object> settingsMap = new HashMap<>();
                        
                        for (Object settingObj : settings) {
                            if (settingObj == null) {
                                continue;
                            }
                            
                            // ModuleSetting cast
                            com.hext.modules.ModuleSetting s = (com.hext.modules.ModuleSetting) settingObj;
                            if (s.name != null && s.value != null) {
                                settingsMap.put(s.name, s.value);
                            }
                        }
                        
                        moduleData.put("settings", settingsMap);
                    }

                    data.put(m.name, moduleData);
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül kayıt hatası (" + m.name + "): " + e.getMessage());
                }
            }

            // JSON yazma
            String json = GSON.toJson(data);
            Files.writeString(CONFIG_PATH, json);
            System.out.println("[HEXT] Config başarıyla kaydedildi!");

        } catch (IOException e) {
            System.err.println("[HEXT] Config yazma hatası: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[HEXT] Config kayıt hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            // File kontrolü
            if (!Files.exists(CONFIG_PATH)) {
                System.out.println("[HEXT] Config dosyası bulunamadı.");
                return;
            }

            // Modules kontrolü
            List<BaseModule> modules = HextClient.modules;
            if (modules == null || modules.isEmpty()) {
                System.err.println("[HEXT] Modules listesi boş!");
                return;
            }

            // JSON okuma
            String json = Files.readString(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) {
                System.out.println("[HEXT] Config dosyası boş!");
                return;
            }

            JsonObject data = GSON.fromJson(json, JsonObject.class);
            if (data == null) {
                System.err.println("[HEXT] JSON parse hatası!");
                return;
            }

            // Her module için ayarları yükle
            for (BaseModule m : modules) {
                if (m == null || m.name == null) {
                    continue;
                }

                try {
                    if (!data.has(m.name)) {
                        continue;
                    }

                    JsonObject moduleData = data.getAsJsonObject(m.name);
                    if (moduleData == null) {
                        continue;
                    }

                    // Enabled durumunu yükle
                    if (moduleData.has("enabled")) {
                        m.enabled = moduleData.get("enabled").getAsBoolean();
                    }

                    // Settings'i yükle
                    if (moduleData.has("settings")) {
                        JsonObject settingsJson = moduleData.getAsJsonObject("settings");
                        List<?> settings = m.getSettings();
                        
                        if (settings != null && !settings.isEmpty()) {
                            for (Object settingObj : settings) {
                                if (settingObj == null) {
                                    continue;
                                }
                                
                                com.hext.modules.ModuleSetting s = (com.hext.modules.ModuleSetting) settingObj;
                                if (s.name == null) {
                                    continue;
                                }

                                if (settingsJson.has(s.name)) {
                                    JsonElement element = settingsJson.get(s.name);
                                    s.value = parseValue(element, s.value);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[HEXT] Modül yükleme hatası (" + m.name + "): " + e.getMessage());
                }
            }

            System.out.println("[HEXT] Config başarıyla yüklendi!");

        } catch (IOException e) {
            System.err.println("[HEXT] Config okuma hatası: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[HEXT] Config yükleme hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * JSON element'i parse eder
     */
    private static Object parseValue(JsonElement element, Object defaultValue) {
        if (element == null) {
            return defaultValue;
        }

        try {
            if (element.isJsonPrimitive()) {
                if (element.getAsJsonPrimitive().isBoolean()) {
                    return element.getAsBoolean();
                }
                
                if (element.getAsJsonPrimitive().isNumber()) {
                    Number num = element.getAsNumber();
                    if (defaultValue instanceof Double) {
                        return num.doubleValue();
                    } else if (defaultValue instanceof Integer) {
                        return num.intValue();
                    } else if (defaultValue instanceof Float) {
                        return num.floatValue();
                    } else if (defaultValue instanceof Long) {
                        return num.longValue();
                    }
                    return num.doubleValue();
                }
                
                if (element.getAsJsonPrimitive().isString()) {
                    return element.getAsString();
                }
            }
        } catch (Exception e) {
            System.err.println("[HEXT] Parse hatası: " + e.getMessage());
        }

        return defaultValue;
    }
}
