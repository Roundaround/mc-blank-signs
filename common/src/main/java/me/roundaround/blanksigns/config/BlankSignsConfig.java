package me.roundaround.blanksigns.config;

import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.trove.config.ConfigPath;
import me.roundaround.trove.config.manage.ModConfigImpl;
import me.roundaround.trove.config.manage.store.GameScopedFileStore;
import me.roundaround.trove.config.option.BooleanConfigOption;

public class BlankSignsConfig extends ModConfigImpl implements GameScopedFileStore {
  private static BlankSignsConfig instance = null;

  public BooleanConfigOption modEnabled;

  public BlankSignsConfig() {
    super(Constants.MOD_ID);
  }

  public static BlankSignsConfig getInstance() {
    if (instance == null) {
      instance = new BlankSignsConfig();
    }
    return instance;
  }

  @Override
  protected void registerOptions() {
    this.modEnabled = this.register(BooleanConfigOption.yesNoBuilder(ConfigPath.of("modEnabled")).setComment(
        "Skip the edit screen when placing signs while sneaking.",
        "If disabled on a server, it is also disabled for all players, regardless of this setting."
    ).setDefaultValue(true).build());
  }
}
