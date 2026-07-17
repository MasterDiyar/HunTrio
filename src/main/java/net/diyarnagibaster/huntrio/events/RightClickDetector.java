package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = HunTrio.MODID)
public class RightClickDetector {

    private static int rightClickHoldTicks = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        // Проверяем, находится ли игрок в игре и активен ли бинд использования предмета
        if (minecraft.player != null && minecraft.options.keyUse.isDown()) {
            rightClickHoldTicks++;

            // Пример: если игрок держит ПКМ ровно 2 секунды (при 20 тиках в секунду)
            if (rightClickHoldTicks == 40) {
                // Выполняем ваше действие здесь
                minecraft.player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("ПКМ удерживается 2 секунды!"), true);
            }
        } else {
            // Сбрасываем таймер, если кнопка не зажата
            rightClickHoldTicks = 0;
        }
    }
}
