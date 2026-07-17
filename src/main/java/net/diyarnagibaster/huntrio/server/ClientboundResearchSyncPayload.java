package net.diyarnagibaster.huntrio.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record ClientboundResearchSyncPayload(List<String> unlockedIds) implements CustomPacketPayload {
    public static final Type<ClientboundResearchSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("huntrio", "sync_research"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundResearchSyncPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            ClientboundResearchSyncPayload::unlockedIds,
            ClientboundResearchSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}