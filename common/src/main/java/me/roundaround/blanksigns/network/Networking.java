package me.roundaround.blanksigns.network;

import me.roundaround.blanksigns.generated.Constants;
import me.roundaround.blanksigns.server.PlayerPreferenceTracker;
import me.roundaround.trove.network.TroveNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public final class Networking {
  public static final Identifier PREFERENCE_C2S = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "preference_c2s");

  public static void register() {
    TroveNetworking.registerC2S(PreferenceC2S.ID, PreferenceC2S.CODEC, (payload, player) ->
        player.level().getServer().execute(() ->
            PlayerPreferenceTracker.getInstance().track(player, payload.preference())));
  }

  public record PreferenceC2S(boolean preference) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PreferenceC2S> ID = new CustomPacketPayload.Type<>(PREFERENCE_C2S);
    public static final StreamCodec<RegistryFriendlyByteBuf, PreferenceC2S> CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        PreferenceC2S::preference,
        PreferenceC2S::new
    );

    @Override
    @NotNull
    public Type<PreferenceC2S> type() {
      return ID;
    }
  }

  private Networking() {
  }
}
