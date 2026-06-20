package com.bindquit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BindQuitClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BindQuitClient.class);
    private static KeyMapping disconnectKey;

    @Override
    public void onInitializeClient() {
        disconnectKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("Disconnect key", GLFW.GLFW_KEY_G, KeyMapping.Category.register(Identifier.parse("bindquit"))));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (disconnectKey.consumeClick()) {
                if (client.isLocalServer() && client.getSingleplayerServer() != null) {
                    client.getSingleplayerServer().halt(true);
                    client.disconnect(new DisconnectedScreen(null, Component.literal(""), Component.literal("You saved and quit")), false);
                } else if (client.getConnection() != null)
                    client.getConnection().getConnection().disconnect(Component.literal("You disconnected"));
            }
        });
        LOGGER.info("BindQuit initialized");
    }
}