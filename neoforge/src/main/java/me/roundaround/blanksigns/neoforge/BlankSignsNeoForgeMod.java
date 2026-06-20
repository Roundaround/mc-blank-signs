package me.roundaround.blanksigns.neoforge;

import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.blanksigns.server.command.BlankSignsCommand;
import me.roundaround.trove.neoforge.TroveNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
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

    // Client setup lives in BlankSignsNeoForgeClient (separate class, not inline) so the dedicated server never links its client classes.
    if (FMLEnvironment.getDist() == Dist.CLIENT) {
      BlankSignsNeoForgeClient.init(modBus, container);
    }
  }
}
