package me.roundaround.blanksigns.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.roundalib.client.gui.screen.ConfigScreen;
import me.roundaround.gradle.api.annotation.Entrypoint;

@Entrypoint(Entrypoint.MOD_MENU)
public class ModMenuImpl implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return (parent) -> new ConfigScreen(parent, Constants.MOD_ID, BlankSignsConfig.getInstance());
  }
}
