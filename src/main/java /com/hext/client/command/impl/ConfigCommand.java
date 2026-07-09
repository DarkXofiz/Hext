package com.darkclient.client.command.impl;

import com.darkclient.client.DarkClient;
import com.darkclient.client.command.Command;
import com.darkclient.client.command.CommandManager;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "Konfigürasyonu kaydet/yükle. Kullanım: .config <save|load>", "cfg");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            CommandManager.sendMessage("\u00A7cKullanım: .config <save|load>");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "save" -> {
                DarkClient.getInstance().getConfigManager().save();
                CommandManager.sendMessage("\u00A7aKonfig kaydedildi.");
            }
            case "load" -> {
                DarkClient.getInstance().getConfigManager().load();
                CommandManager.sendMessage("\u00A7aKonfig yüklendi.");
            }
            default -> CommandManager.sendMessage("\u00A7cGeçersiz eylem: " + args[0]);
        }
    }
}
