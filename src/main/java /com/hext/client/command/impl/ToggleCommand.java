package com.hext.client.command.impl;

import com.hext.client.Hext;
import com.hext.client.command.Command;
import com.hext.client.command.CommandManager;
import com.hext.client.module.Module;

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
        Optional<Module> opt = Hext.getInstance().getModuleManager().getModule(name);
        if (opt.isPresent()) {
            opt.get().toggle();
            CommandManager.sendMessage("\u00A75" + opt.get().getName()
                    + "\u00A77 " + (opt.get().isEnabled() ? "\u00A7aaktif" : "\u00A7cdevre dışı"));
        } else {
            CommandManager.sendMessage("\u00A7cModül bulunamadı: " + name);
        }
    }
}
