package com.hextclient.client.command;

import com.hextclient.client.command.impl.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.*;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();
    public static final String PREFIX = ".";

    public CommandManager() {
        register(
            new HelpCommand(),
            new ToggleCommand(),
            new BindCommand(),
            new ConfigCommand(),
            new ThemeCommand()
        );
    }

    private void register(Command... cmds) {
        commands.addAll(Arrays.asList(cmds));
    }

    public boolean handleMessage(String message) {
        if (!message.startsWith(PREFIX)) return false;
        String[] parts = message.substring(PREFIX.length()).split(" ");
        if (parts.length == 0) return true;
        String name = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)
                    || Arrays.asList(cmd.getAliases()).contains(name)) {
                cmd.execute(args);
                return true;
            }
        }
        sendMessage("\u00A7cUnknown command. Type .help for a list.");
        return true;
    }

    public static void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }

    public List<Command> getCommands() { return Collections.unmodifiableList(commands); }
}
