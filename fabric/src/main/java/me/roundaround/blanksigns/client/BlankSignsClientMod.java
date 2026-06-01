package me.roundaround.blanksigns.client;

import me.roundaround.allay.api.Entrypoint;
import me.roundaround.blanksigns.client.network.ClientNetworking;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Entrypoint(Entrypoint.CLIENT)
public class BlankSignsClientMod implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ClientNetworking.sendPreference(
        BlankSignsConfig.getInstance().modEnabled.getValue()));

    BlankSignsConfig.getInstance().modEnabled.pendingValue.cold()
        .subscribe(ClientNetworking::sendPreference);
  }
}
