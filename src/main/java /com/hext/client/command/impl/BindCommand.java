package com.hext.client.command.impl;

import com.hext.client.HextClient;
import com.hext.client.command.Command;
import com.hext.client.command.CommandManager;
import com.hext.client.module.Module;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.Optional;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Modüle tuş atar. Kullanım: .bind <modül> <tuş>", "b");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            CommandManager.sendMessage("\u00A7cKullanım: .bind <modül> <tuş>");
            return;
        }
        String modName = args[0];
        String keyName = args[1].toUpperCase();
        Optional<Module> opt = HextClient.getInstance().getModuleManager().getModule(modName);
        if (opt.isEmpty()) {
            CommandManager.sendMessage("\u00A7cModül bulunamadı: " + modName);
            return;
        }
        try {
            Field f = GLFW.class.getField("GLFW_KEY_" + keyName);
            int keyCode = f.getInt(null);
            opt.get().setKeybind(keyCode);
            CommandManager.sendMessage("\u00A75" + modName + "\u00A77 tuşu \u00A75" + keyName + "\u00A77 olarak ayarlandı.");
        } catch (Exception e) {
            CommandManager.sendMessage("\u00A7cGeçersiz tuş: " + keyName);
        }
    }
}
