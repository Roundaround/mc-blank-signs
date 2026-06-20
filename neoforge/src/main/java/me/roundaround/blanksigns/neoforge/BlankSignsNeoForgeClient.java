package me.roundaround.blanksigns.neoforge;

import me.roundaround.blanksigns.client.network.ClientNetworking;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

// Separate class (not an inline Dist gate in the @Mod ctor) so the dedicated server never links its client classes.
public final class BlankSignsNeoForgeClient {
  private BlankSignsNeoForgeClient() {
  }

  public static void init(IEventBus modBus, ModContainer container) {
    modBus.addListener(FMLClientSetupEvent.class, event -> {
      NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, joinEvent ->
          ClientNetworking.sendPreference(BlankSignsConfig.getInstance().modEnabled.getValue()));

      BlankSignsConfig.getInstance().modEnabled.pendingValue.cold()
          .subscribe(ClientNetworking::sendPreference);
    });

    container.registerExtensionPoint(IConfigScreenFactory.class,
        (modContainer, parent) -> new ConfigScreen(parent, Constants.MOD_ID, BlankSignsConfig.getInstance()));
  }
}
