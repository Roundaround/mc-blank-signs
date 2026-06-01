package me.roundaround.blanksigns.neoforge;

import me.roundaround.blanksigns.client.network.ClientNetworking;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.blanksigns.server.command.BlankSignsCommand;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import me.roundaround.trove.neoforge.TroveNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod("blanksigns")
public final class BlankSignsNeoForgeMod {
  public BlankSignsNeoForgeMod(IEventBus modBus, ModContainer container) {
    TroveNeoForge.bootstrap(modBus, container);
    BlankSignsConfig.getInstance().init();
    Networking.register();

    NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> {
      BlankSignsCommand.register(event.getDispatcher());
    });

    NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> {
      PlayerPreferenceTracker.getInstance().remove(event.getEntity());
    });

    modBus.addListener(FMLClientSetupEvent.class, event -> {
      // Send the local preference on join, and again whenever the user toggles the option in-game.
      NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, joinEvent ->
          ClientNetworking.sendPreference(BlankSignsConfig.getInstance().modEnabled.getValue()));

      BlankSignsConfig.getInstance().modEnabled.pendingValue.cold()
          .subscribe(ClientNetworking::sendPreference);
    });

    container.registerExtensionPoint(IConfigScreenFactory.class,
        (modContainer, parent) -> new ConfigScreen(parent, Constants.MOD_ID, BlankSignsConfig.getInstance()));
  }
}
