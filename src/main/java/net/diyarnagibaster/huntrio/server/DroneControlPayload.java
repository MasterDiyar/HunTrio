package net.diyarnagibaster.huntrio.server;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.component.ModDataComponents;
import net.diyarnagibaster.huntrio.entity.DroneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record DroneControlPayload(boolean w, boolean s, boolean a, boolean d, boolean space, boolean shift) implements CustomPacketPayload {
    public static final Type<DroneControlPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HunTrio.MODID, "drone_control"));

    // Кодек, который упаковывает и распаковывает наши кнопки в байты
    public static final StreamCodec<FriendlyByteBuf, DroneControlPayload> STREAM_CODEC = StreamCodec.ofMember(
            DroneControlPayload::write, DroneControlPayload::new
    );

    // Распаковка
    public DroneControlPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    // Упаковка
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(w); buf.writeBoolean(s); buf.writeBoolean(a); buf.writeBoolean(d); buf.writeBoolean(space); buf.writeBoolean(shift);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // ЛОГИКА СЕРВЕРА: Что делать, когда пакет пришел?
    public static void handleData(DroneControlPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            var stack = player.getMainHandItem();

            // Ищем UUID дрона в джойстике
            UUID uuid = stack.get(ModDataComponents.BOUND_DRONE.get());
            if (uuid != null && player.level() instanceof ServerLevel serverLevel) {
                var entity = serverLevel.getEntity(uuid);
                if (entity instanceof DroneEntity drone) {
                    // Передаем нажатые кнопки самому дрону!
                    drone.updateMovement(payload.w(), payload.s(), payload.a(), payload.d(), payload.space(), payload.shift());
                }
            }
        });
    }
}