package me.roundaround.blanksigns.gametest;

import me.roundaround.allay.api.gametest.ClientGameTest;
import me.roundaround.trove.gametest.ClientTest;
import me.roundaround.trove.gametest.ClientTestContext;
import me.roundaround.trove.gametest.ClientWorld;
import me.roundaround.trove.gametest.GameTestAssertionException;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SignBlock;

/**
 * Exercises the mod's core path: sneak-placing a sign skips the text-edit screen,
 * leaving it blank. Boots a client world, sneak-places a sign on a block, and
 * asserts the sign exists but no {@link AbstractSignEditScreen} opened — which is
 * exactly what {@code SignItemMixin#shouldOpenEditScreen} suppresses for a sneaking
 * single-player placement.
 */
@ClientGameTest
public class BlankSignsClientUsageTest implements ClientTest {
  @Override
  public void runTest(ClientTestContext context) {
    try (ClientWorld world = context.worldBuilder().creative().stopTime(true).create()) {
      world.fill(new BlockPos(0, 64, 0), new BlockPos(3, 64, 3), "minecraft:smooth_stone");
      world.teleport(0.5, 65.0, 0.5);
      world.setMainHandItem("minecraft:oak_sign");
      context.waitTicks(2);

      BlockPos support = new BlockPos(1, 64, 1);
      BlockPos signPos = support.above();

      // Hold sneak across the placement so the server player reports a secondary-use
      // active interaction — the trigger the mod keys its blank-sign behavior off.
      context.runOnClient((mc) -> mc.options.keyShift.setDown(true));
      context.waitTicks(2);
      world.useItemOn(support);
      world.settle();
      context.runOnClient((mc) -> mc.options.keyShift.setDown(false));

      if (!(world.getBlock(signPos) instanceof SignBlock)) {
        throw new GameTestAssertionException("sneak-placed sign block is missing");
      }
      if (context.currentScreen() instanceof AbstractSignEditScreen) {
        throw new GameTestAssertionException("blank-sign placement still opened the edit screen");
      }
    }
  }
}
