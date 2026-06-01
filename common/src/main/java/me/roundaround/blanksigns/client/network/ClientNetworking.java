package me.roundaround.blanksigns.client.network;

import me.roundaround.blanksigns.network.Networking;
import me.roundaround.trove.network.TroveNetworking;

public final class ClientNetworking {
  public static void sendPreference(boolean preference) {
    TroveNetworking.sendToServer(new Networking.PreferenceC2S(preference));
  }

  private ClientNetworking() {
  }
}
