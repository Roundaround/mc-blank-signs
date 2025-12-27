package me.roundaround.blanksigns.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class BlankSignsCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal(Constants.MOD_ID)
        .then(reloadSub())
        .then(enableSub())
        .then(disableSub()));
  }

  private static LiteralArgumentBuilder<ServerCommandSource> reloadSub() {
    return CommandManager.literal("reload")
        .requires((source) -> source.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.syncWithStore();
          context.getSource().sendFeedback(
              () -> Text.translatable(
                  "blanksigns.commands.reload",
                  String.valueOf(config.modEnabled.getValue().booleanValue())
              ), true
          );
          return 1;
        });
  }

  private static LiteralArgumentBuilder<ServerCommandSource> enableSub() {
    return CommandManager.literal("enable")
        .requires((source) -> source.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.modEnabled.setValue(true);

          if (config.isDirty()) {
            config.writeToStore();
            context.getSource().sendFeedback(
                () -> Text.translatable(context.getSource().getServer().isSingleplayer() ?
                    "blanksigns.commands.enable" :
                    "blanksigns.commands.enable.server"), true
            );
            return 0;
          }

          context.getSource().sendFeedback(() -> Text.translatable("blanksigns.commands.enable.noop"), false);
          return 1;
        });
  }

  private static LiteralArgumentBuilder<ServerCommandSource> disableSub() {
    return CommandManager.literal("disable")
        .requires((source) -> source.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.modEnabled.setValue(false);

          if (config.isDirty()) {
            config.writeToStore();
            context.getSource().sendFeedback(
                () -> Text.translatable(context.getSource().getServer().isSingleplayer() ?
                    "blanksigns.commands.disable" :
                    "blanksigns.commands.disable.server"), true
            );
            return 0;
          }

          context.getSource().sendFeedback(() -> Text.translatable("blanksigns.commands.disable.noop"), false);
          return 1;
        });
  }

  private BlankSignsCommand() {
  }
}
