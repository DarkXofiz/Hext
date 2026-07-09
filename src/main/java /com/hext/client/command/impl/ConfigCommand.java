package com.hext.client.command.impl;

import com.hext.client.HextClient;
import com.hext.client.command.Command;
import com.hext.client.command.CommandManager;

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
                HextClient.getInstance().getConfigManager().save();
                CommandManager.sendMessage("\u00A7aKonfig kaydedildi.");
            }
            case "load" -> {
                HextClient.getInstance().getConfigManager().load();
                CommandManager.sendMessage("\u00A7aKonfig yüklendi.");
            }
            default -> CommandManager.sendMessage("\u00A7cGeçersiz eylem: " + args[0]);
        }
    }
}
