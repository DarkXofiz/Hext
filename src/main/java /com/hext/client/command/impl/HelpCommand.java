package com.darkclient.client.command.impl;

import com.darkclient.client.DarkClient;
import com.darkclient.client.command.Command;
import com.darkclient.client.command.CommandManager;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Tüm komutları listeler", "h", "?");
    }

    @Override
    public void execute(String[] args) {
        CommandManager.sendMessage("\u00A75\u00A7l=== DarkClient Commands ===");
        DarkClient.getInstance().getCommandManager().getCommands().forEach(cmd ->
            CommandManager.sendMessage("\u00A75." + cmd.getName() + "\u00A77 - " + cmd.getDescription())
        );
    }
}
