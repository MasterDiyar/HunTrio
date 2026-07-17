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
            LITHIUM_INGOT = ITEMS.register("lithium_ingot", () -> new Item(new Item.Properties())),
            NETHER_SILK = ITEMS.register("nether_silk", () -> new Item(new Item.Properties())),
            COAL_DUST = ITEMS.register("coal_dust", () -> new Item(new Item.Properties())),
            GRAPHITE_DUST = ITEMS.register("graphite_dust", () -> new Item(new Item.Properties())),
            GRAPHITE = ITEMS.register("graphite", () -> new Item(new Item.Properties())),
            NICKEL_ORE  = ITEMS.register("nickel_ore", () -> new Item(new Item.Properties())),
            NICKEL = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties())),
            MANGANESE  = ITEMS.register("manganese_ingot", () -> new Item(new Item.Properties())),
            COBALT_ORE  = ITEMS.register("cobalt_ore", () -> new Item(new Item.Properties())),
            COBALT  = ITEMS.register("cobalt_ingot", () -> new Item(new Item.Properties())),
            SULFUR = ITEMS.register("sulfur", () -> new Item(new Item.Properties())),
            TELLURIUM_ORE = ITEMS.register("tellurium_ore", () -> new Item(new Item.Properties())),
            TELLURIUM = ITEMS.register("tellurium_ingot", () -> new Item(new Item.Properties())),
            ALUMINIUM_BRUSH = ITEMS.register("aluminium_brush",
            () -> new CustomBrushItem(new Item.Properties().durability(198), 35)),
            NETHERITE_BRUSH = ITEMS.register("netherite_brush",
            () -> new CustomBrushItem(new Item.Properties().durability(1125), 50)),
            RESEARCH_BOOK = ITEMS.register("research_book",
                    ()-> new ResearchBookItem(new Item.Properties()));

    public static void register(IEventBus eventBus){

        ITEMS.register(eventBus);

    }
}
