package me.roundaround.blanksigns.server;

import java.util.HashSet;
import net.minecraft.world.entity.player.Player;

public final class PlayerPreferenceTracker {
  private static PlayerPreferenceTracker instance;

  private final HashSet<String> forcedOff = new HashSet<>();

  public void track(Player player, boolean preference) {
    String id = player.getStringUUID();
    if (preference) {
      this.forcedOff.remove(id);
    } else {
      this.forcedOff.add(id);
    }
  }

  public void remove(Player player) {
    this.forcedOff.remove(player.getStringUUID());
  }

  public boolean disabledForPlayer(Player player) {
    return this.forcedOff.contains(player.getStringUUID());
  }

  public static PlayerPreferenceTracker getInstance() {
    if (instance == null) {
      instance = new PlayerPreferenceTracker();
    }
    return instance;
  }

  private PlayerPreferenceTracker() {
  }
}
