package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.entity.ModBlockEntities;
import net.diyarnagibaster.huntrio.item.ElectroItem;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = HunTrio.MODID)
public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,       // Какой тип Capability регистрируем
                ModBlockEntities.ELECTRIC_FURNACE.get(),// Для какого BlockEntity
                (blockEntity, side) -> blockEntity.energyStorage // Возвращаем хранилище из нашего класса
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.ELECTRIC_FURNACE.get(),
                (blockEntity, side) -> blockEntity.inventory
        );

        // Регистрируем энергию для картофельной батарейки
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> {
                    if (stack.getItem() instanceof ElectroItem electroItem) {
                        return new ElectroItem.EnergyStorageWrapper(stack, electroItem);
                    }
                    return null;
                },
                ModItems.POTATO_BATTERY.get() // Сюда добавляй все свои предметы-батарейки через запятую
        );
    }
}