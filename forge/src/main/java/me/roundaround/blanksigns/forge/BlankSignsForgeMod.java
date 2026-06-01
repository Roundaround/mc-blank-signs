package me.roundaround.blanksigns.forge;

import me.roundaround.blanksigns.client.network.ClientNetworking;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.blanksigns.network.Networking;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.blanksigns.server.command.BlankSignsCommand;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

    FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(event -> {
      // Send the local preference on join, and again whenever the user toggles the option in-game.
      ClientPlayerNetworkEvent.LoggingIn.BUS.addListener(joinEvent ->
          ClientNetworking.sendPreference(BlankSignsConfig.getInstance().modEnabled.getValue()));

      BlankSignsConfig.getInstance().modEnabled.pendingValue.cold()
          .subscribe(ClientNetworking::sendPreference);
    });

    context.getContainer().registerExtensionPoint(
        ConfigScreenHandler.ConfigScreenFactory.class,
        () -> new ConfigScreenHandler.ConfigScreenFactory(
            (mc, parent) -> new ConfigScreen(parent, Constants.MOD_ID, BlankSignsConfig.getInstance())));
  }
}
