package com.hext.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hext.HextClient;
import com.hext.modules.BaseModule;

import java.io.*;
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
            Map<String, Boolean> data = new HashMap<>();
            HextClient.modules.forEach(m -> data.put(m.name, m.enabled));
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) return;
        try {
            String json = Files.readString(CONFIG_PATH);
            Map<String, Boolean> data = GSON.fromJson(json, Map.class);
            HextClient.modules.forEach(m -> {
                if (data.containsKey(m.name)) m.enabled = data.get(m.name);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
