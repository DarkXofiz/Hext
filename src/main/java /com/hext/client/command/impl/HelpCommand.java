package com.hextclient.client.command.impl;

import com.hextclient.client.HextClient;
import com.hextclient.client.command.Command;
import com.hextclient.client.command.CommandManager;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Tüm komutları listeler", "h", "?");
    }

    @Override
    public void execute(String[] args) {
        CommandManager.sendMessage("\u00A75\u00A7l=== HextClient Commands ===");
        HextClient.getInstance().getCommandManager().getCommands().forEach(cmd ->
            CommandManager.sendMessage("\u00A75." + cmd.getName() + "\u00A77 - " + cmd.getDescription())
        );
    }
}
