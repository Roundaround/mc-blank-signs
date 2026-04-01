package me.roundaround.blanksigns.server.network;

import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ServerNetworking {
  public static void registerReceivers() {
    ServerPlayNetworking.registerGlobalReceiver(Networking.PreferenceC2S.ID, ServerNetworking::handlePreference);
  }

  private static void handlePreference(Networking.PreferenceC2S payload, ServerPlayNetworking.Context context) {
    final ServerPlayer player = context.player();
    context.server().execute(() -> {
      PlayerPreferenceTracker.getInstance().track(player, payload.preference());
    });
  }

  private ServerNetworking() {
  }
}
