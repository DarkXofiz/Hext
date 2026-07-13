package com.hext.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        try {
            if (HextClient.modules == null || HextClient.modules.isEmpty()) {
                return;
            }

            Map<String, Object> data = new HashMap<>();

            for (int i = 0; i < HextClient.modules.size(); i++) {
                BaseModule m = HextClient.modules.get(i);
                if (m == null || m.name == null) {
                    continue;
                }

                Map<String, Object> moduleMap = new HashMap<>();
                moduleMap.put("enabled", m.enabled);
                moduleMap.put("settings", new HashMap<>());

                data.put(m.name, moduleMap);
            }

            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, GSON.toJson(data).getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH) || HextClient.modules == null || HextClient.modules.isEmpty()) {
                return;
            }

            String json = new String(Files.readAllBytes(CONFIG_PATH));
            JsonObject root = GSON.fromJson(json, JsonObject.class);

            if (root == null) {
                return;
            }

            for (int i = 0; i < HextClient.modules.size(); i++) {
                BaseModule m = HextClient.modules.get(i);
                if (m == null || m.name == null) {
                    continue;
                }

                if (root.has(m.name)) {
                    JsonObject moduleObj = root.getAsJsonObject(m.name);
                    if (moduleObj.has("enabled")) {
                        m.enabled = moduleObj.get("enabled").getAsBoolean();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
