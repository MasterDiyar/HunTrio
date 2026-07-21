package net.diyarnagibaster.huntrio.gui;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, HunTrio.MODID);

    public static final Supplier<MenuType<ElectricFurnaceMenu>> ELECTRIC_FURNACE_MENU =
            MENUS.register("electric_furnace_menu", () ->
                    net.neoforged.neoforge.common.extensions.IMenuTypeExtension.create(ElectricFurnaceMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
