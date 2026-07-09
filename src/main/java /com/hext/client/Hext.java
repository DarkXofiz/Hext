package com.hext.client;

import com.hext.client.command.CommandManager;
import com.hext.client.config.ConfigManager;
import com.hext.client.event.EventBus;
import com.hext.client.gui.clickgui.ClickGUI;
import com.hext.client.gui.hud.HudManager;
import com.hext.client.gui.theme.ThemeManager;
import com.hext.client.module.ModuleManager;
import com.hext.client.util.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hext implements ClientModInitializer {

    public static final String NAME = "HextClient";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static HextClient instance;

    private EventBus eventBus;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private ThemeManager themeManager;
    private HudManager hudManager;
    private ClickGUI clickGUI;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("{} v{} loading...", NAME, VERSION);

        eventBus = new EventBus();
        themeManager = new ThemeManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        hudManager = new HudManager();
        configManager = new ConfigManager();
        clickGUI = new ClickGUI();

        configManager.load();
        new KeyInputHandler();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            moduleManager.onTick(client);
            hudManager.onTick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen == null || clickGUI.isOpen()) {
                hudManager.render(drawContext, tickDelta);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
        LOGGER.info("{} v{} loaded successfully!", NAME, VERSION);
    }

    public static HextClient getInstance() { return instance; }
    public EventBus getEventBus() { return eventBus; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public CommandManager getCommandManager() { return commandManager; }
    public ThemeManager getThemeManager() { return themeManager; }
    public HudManager getHudManager() { return hudManager; }
    public ClickGUI getClickGUI() { return clickGUI; }
}
