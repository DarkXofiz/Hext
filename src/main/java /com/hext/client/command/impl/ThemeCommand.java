package com.darkclient.client.command.impl;

import com.darkclient.client.DarkClient;
import com.darkclient.client.command.Command;
import com.darkclient.client.command.CommandManager;

public class ThemeCommand extends Command {

    public ThemeCommand() {
        super("theme", "Tema değiştir. Kullanım: .theme <Dark|Purple|Red|Cyan>", "color");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            CommandManager.sendMessage("\u00A7cKullanım: .theme <Dark|Purple|Red|Cyan>");
            return;
        }
        String theme = args[0];
        DarkClient.getInstance().getThemeManager().setTheme(theme);
        CommandManager.sendMessage("\u00A75Tema \u00A7a" + theme + "\u00A75 olarak ayarlandı.");
    }
}
