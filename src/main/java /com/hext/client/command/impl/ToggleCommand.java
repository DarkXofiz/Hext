package com.darkclient.client.command.impl;

import com.darkclient.client.DarkClient;
import com.darkclient.client.command.Command;
import com.darkclient.client.command.CommandManager;
import com.darkclient.client.module.Module;

import java.util.Optional;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "Bir modülü açar/kapatır. Kullanım: .toggle <modül>", "t");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            CommandManager.sendMessage("\u00A7cKullanım: .toggle <modül adı>");
            return;
        }
        String name = String.join(" ", args);
        Optional<Module> opt = DarkClient.getInstance().getModuleManager().getModule(name);
        if (opt.isPresent()) {
            opt.get().toggle();
            CommandManager.sendMessage("\u00A75" + opt.get().getName()
                    + "\u00A77 " + (opt.get().isEnabled() ? "\u00A7aaktif" : "\u00A7cdevre dışı"));
        } else {
            CommandManager.sendMessage("\u00A7cModül bulunamadı: " + name);
        }
    }
}
