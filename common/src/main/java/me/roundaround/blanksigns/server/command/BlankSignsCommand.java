package me.roundaround.blanksigns.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.generated.Constants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public final class BlankSignsCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal(Constants.MOD_ID)
        .then(reloadSub())
        .then(enableSub())
        .then(disableSub()));
  }

  private static LiteralArgumentBuilder<CommandSourceStack> reloadSub() {
    return Commands.literal("reload")
        .requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.syncWithStore();
          context.getSource().sendSuccess(
              () -> Component.translatable(
                  "blanksigns.commands.reload",
                  String.valueOf(config.modEnabled.getValue().booleanValue())
              ), true
          );
          return 1;
        });
  }

  private static LiteralArgumentBuilder<CommandSourceStack> enableSub() {
    return Commands.literal("enable")
        .requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.modEnabled.setValue(true);

          if (config.isDirty()) {
            config.writeToStore();
            context.getSource().sendSuccess(
                () -> Component.translatable(context.getSource().getServer().isSingleplayer() ?
                    "blanksigns.commands.enable" :
                    "blanksigns.commands.enable.server"), true
            );
            return 0;
          }

          context.getSource().sendSuccess(() -> Component.translatable("blanksigns.commands.enable.noop"), false);
          return 1;
        });
  }

  private static LiteralArgumentBuilder<CommandSourceStack> disableSub() {
    return Commands.literal("disable")
        .requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
        .executes((context) -> {
          BlankSignsConfig config = BlankSignsConfig.getInstance();
          config.modEnabled.setValue(false);

          if (config.isDirty()) {
            config.writeToStore();
            context.getSource().sendSuccess(
                () -> Component.translatable(context.getSource().getServer().isSingleplayer() ?
                    "blanksigns.commands.disable" :
                    "blanksigns.commands.disable.server"), true
            );
            return 0;
          }

          context.getSource().sendSuccess(() -> Component.translatable("blanksigns.commands.disable.noop"), false);
          return 1;
        });
  }

  private BlankSignsCommand() {
  }
}
