package com.hext.client.command.impl;

import com.hext.client.Hext;
import com.hext.client.command.Command;
import com.hext.client.command.CommandManager;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Tüm komutları listeler", "h", "?");
    }

    @Override
    public void execute(String[] args) {
        CommandManager.sendMessage("\u00A75\u00A7l=== Hext Commands ===");
        Hext.getInstance().getCommandManager().getCommands().forEach(cmd ->
            CommandManager.sendMessage("\u00A75." + cmd.getName() + "\u00A77 - " + cmd.getDescription())
        );
    }
}
