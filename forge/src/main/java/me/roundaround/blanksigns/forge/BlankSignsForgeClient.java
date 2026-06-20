package me.roundaround.blanksigns.forge;

import me.roundaround.blanksigns.client.network.ClientNetworking;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// Separate class (not an inline Dist gate in the @Mod ctor) so the dedicated server never links its client classes.
public final class BlankSignsForgeClient {
  private BlankSignsForgeClient() {
  }

  public static void init(FMLJavaModLoadingContext context) {
    FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(event -> {
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
