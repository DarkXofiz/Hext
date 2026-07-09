package com.hext.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hext implements ClientModInitializer {
    public static final String NAME = "Hext";
    public static final String VERSION = "1.21x";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public void onInitializeClient() {
        LOGGER.info("{} v{} loaded!", NAME, VERSION);
    }
}
