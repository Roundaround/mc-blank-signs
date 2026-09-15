package me.roundaround.blanksigns.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.roundaround.blanksigns.config.BlankSignsConfig;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// 26.3: SignItem is gone; the placement-time editor open now lives in SignBlock#setPlacedBy.
@Mixin(SignBlock.class)
public class SignBlockMixin {
  @WrapWithCondition(
      method = "setPlacedBy", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/level/block/SignBlock;openTextEdit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/world/level/block/entity/SignTextSlot;)V"
  )
  )
  public boolean shouldOpenEditScreen(
      SignBlock instance,
      Player player,
      SignBlockEntity blockEntity,
      SignTextSlot slot,
      @Local(argsOnly = true) Level world
  ) {
    MinecraftServer server = world.getServer();
    if (server == null) {
      return false;
    }

    if (!player.isSecondaryUseActive()) {
      return true;
    }


    // If in single player, just use the config value directly. If the mod is disabled server-side, then it is disabled
    // for everyone, regardless of their preference
    boolean disabled = !BlankSignsConfig.getInstance().modEnabled.getValue();
    if (server.isSingleplayer() || disabled) {
      return disabled;
    }

    // At this point we know 1. the player is sneaking, 2. we're in multiplayer, and 3. the mod is enabled server-side
    return PlayerPreferenceTracker.getInstance().disabledForPlayer(player);
  }
}
