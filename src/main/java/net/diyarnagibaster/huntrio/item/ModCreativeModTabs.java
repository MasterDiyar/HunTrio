package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HunTrio.MODID);

    public static final Supplier<CreativeModeTab> HUNTRIOTAB = CREATIVE_MODE_TAB.register("huntrio_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.ALUMINIUM_INGOT.get())).
            title(Component.translatable("creativetab.huntrio.huntriotab"))
                    .displayItems((itemDisplayParameters, output) ->{
                        output.accept(ModItems.ALUMINIUM);
                        output.accept(ModItems.ALUMINIUM_INGOT);
                        output.accept(ModBlocks.ALUMINIUM_BLOCK);
                        output.accept(ModBlocks.DESICCANT);
                        output.accept(ModItems.LITHIUM_ORE);
                        output.accept(ModItems.LITHIUM_INGOT);
                        output.accept(ModItems.NETHER_SILK);
                        output.accept(ModItems.ALUMINIUM_BRUSH);
                        output.accept(ModItems.NETHERITE_BRUSH);
                        output.accept(ModItems.COAL_DUST);
                        output.accept(ModItems.GRAPHITE_DUST);
                        output.accept(ModItems.GRAPHITE);
                        output.accept(ModItems.NICKEL);
                        output.accept(ModItems.NICKEL_ORE);
                        output.accept(ModItems.COBALT);
                        output.accept(ModItems.COBALT_ORE);
                        output.accept(ModItems.MANGANESE);
                        output.accept(ModItems.TELLURIUM);
                        output.accept(ModItems.TELLURIUM_ORE);
                        output.accept(ModItems.SULFUR);
                        output.accept(ModBlocks.RESEARCH_TABLE);
                        output.accept(ModItems.RESEARCH_BOOK);
                        output.accept(ModItems.POTATO_BATTERY);
                        output.accept(ModItems.SULFUR_JAR);
                        output.accept(ModItems.SALT_JAR);
                        output.accept(ModItems.JOYSTICK);
                        output.accept(ModItems.ROBOT_SPAWN_EGG);
                        //output.accept(ModItems.);
                    }).build());

    public static void Register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }


}
