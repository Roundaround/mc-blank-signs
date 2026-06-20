package me.roundaround.blanksigns.forge;

import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.blanksigns.server.command.BlankSignsCommand;
import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("blanksigns")
public final class BlankSignsForgeMod {
  public BlankSignsForgeMod(FMLJavaModLoadingContext context) {
    TroveForge.bootstrap(context);
    BlankSignsConfig.getInstance().init();
    Networking.register();

    RegisterCommandsEvent.BUS.addListener(event -> {
      BlankSignsCommand.register(event.getDispatcher());
    });

    PlayerEvent.PlayerLoggedOutEvent.BUS.addListener(event -> {
      PlayerPreferenceTracker.getInstance().remove(event.getEntity());
    });

    // Client setup lives in BlankSignsForgeClient (separate class, not inline) so the dedicated server never links its client classes.
    if (FMLEnvironment.dist.isClient()) {
      BlankSignsForgeClient.init(context);
    }
  }
}
