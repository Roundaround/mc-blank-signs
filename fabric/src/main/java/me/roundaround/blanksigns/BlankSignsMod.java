package me.roundaround.blanksigns;

import me.roundaround.allay.api.Entrypoint;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.blanksigns.server.command.BlankSignsCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

@Entrypoint(Entrypoint.MAIN)
public final class BlankSignsMod implements ModInitializer {
  @Override
  public void onInitialize() {
    BlankSignsConfig.getInstance().init();
    Networking.register();

    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
      BlankSignsCommand.register(dispatcher);
    });

    ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
      PlayerPreferenceTracker.getInstance().remove(handler.getPlayer());
    });
  }
}
