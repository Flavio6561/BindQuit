package com.bindquit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BindQuitClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(BindQuitClient.class);
	private static KeyBinding disconnectKey;

	@Override
	public void onInitializeClient() {
		LOGGER.info("BindQuit initialized with success");
		disconnectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Quit bind",
				GLFW.GLFW_KEY_K,
				"BindQuit"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (disconnectKey.wasPressed()) {
				if (client.world != null && !client.isInSingleplayer()) {
					client.disconnect(new DisconnectedScreen(null, Text.literal(""), Text.literal("You successfully disconnected")));
				} else if (client.isInSingleplayer()) {
					MinecraftServer server = client.getServer();
					if (server != null) {
						server.stop(true);
						client.disconnect(new DisconnectedScreen(null, Text.literal(""), Text.literal("You successfully saved and quit")));
					}
				}
			}
		});
	}
}