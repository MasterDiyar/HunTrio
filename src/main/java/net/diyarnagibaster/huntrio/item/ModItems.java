package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HunTrio.MODID);

    public static final DeferredItem<Item>
            ALUMINIUM = ITEMS.register("aluminium", () -> new Item(new Item.Properties())),
            ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties())),
            LITHIUM_ORE = ITEMS.register("lithium_ore", () -> new Item(new Item.Properties())),
            LITHIUM_INGOT = ITEMS.register("lithium_ingot", () -> new Item(new Item.Properties()));;


    public static void register(IEventBus eventBus){

        ITEMS.register(eventBus);

    }
}
