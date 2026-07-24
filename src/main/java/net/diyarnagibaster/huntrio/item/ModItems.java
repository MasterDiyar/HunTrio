package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.entity.ModEntities;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;

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
            SULFUR_JAR = ITEMS.register("sulfur_in_a_jar", () -> new PotionItem(new Item.Properties())),
            TELLURIUM_ORE = ITEMS.register("tellurium_ore", () -> new Item(new Item.Properties())),
            TELLURIUM = ITEMS.register("tellurium_ingot", () -> new Item(new Item.Properties())),
            SALT_JAR = ITEMS.register("salt_in_a_jar", () -> new Item(new Item.Properties())),
            SILICON_SLUG = ITEMS.register("silicon_slug", () -> new Item(new Item.Properties())),
            DIOD = ITEMS.register("diod", () -> new Item(new Item.Properties())),
            POTATO_BATTERY = ITEMS.register(("potato_battery"), () -> new ElectroItem(new Item.Properties().stacksTo(1), 10000, 100, 100)),
            JOYSTICK = ITEMS.register("joystick", ()-> new JoystickItem(new Item.Properties())),
            ALUMINIUM_BRUSH = ITEMS.register("aluminium_brush",
            () -> new CustomBrushItem(new Item.Properties().durability(198), 35)),
            NETHERITE_BRUSH = ITEMS.register("netherite_brush",
            () -> new CustomBrushItem(new Item.Properties().durability(1125), 50)),
            RESEARCH_BOOK = ITEMS.register("research_book",
                    ()-> new ResearchBookItem(new Item.Properties())),
            ROBOT_SPAWN_EGG = ITEMS.register("robot_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DRONE_MK1, 0x7A7A7A, 0x1F1F1F, new Item.Properties())),
            ALNIMAGNET = ITEMS.register("alni_magnet", () -> new Item(new Item.Properties())),
            SALT_PICKAXE = ITEMS.register("salt_pickaxe",
                    () -> new CustomPickaxeItem.Builder(Tiers.IRON, new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1.0F, -2.8F)))
                            .onBreak(Blocks.STONE, ModItems.NICKEL_ORE.get(), 0.5f).build()),
            GRAPHITE_RAPIER = ITEMS.register("graphite_rapier", () -> new SwordItem(ModTiers.GRAPHITE, new Item.Properties()
                    .durability(320).stacksTo(1).attributes(SwordItem.createAttributes(ModTiers.GRAPHITE, 3, -2))));


    public static void register(IEventBus eventBus){

        ITEMS.register(eventBus);

    }
}
