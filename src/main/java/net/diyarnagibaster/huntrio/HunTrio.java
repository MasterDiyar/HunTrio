package net.diyarnagibaster.huntrio;

import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.diyarnagibaster.huntrio.component.ModDataComponents;
import net.diyarnagibaster.huntrio.entity.ModBlockEntities;
import net.diyarnagibaster.huntrio.entity.ModEntities;
import net.diyarnagibaster.huntrio.gui.ModMenus;
import net.diyarnagibaster.huntrio.item.ModCreativeModTabs;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.diyarnagibaster.huntrio.research.ModAttachments;
import net.diyarnagibaster.huntrio.research.ModRecipes;
import net.diyarnagibaster.huntrio.server.ModNetworkHandler;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(HunTrio.MODID)
public class HunTrio {
    public static final String MODID = "huntrio";

    public static final Logger LOGGER = LogUtils.getLogger();

    public HunTrio(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);



        ModCreativeModTabs.Register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModAttachments.ATTACHMENTS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModDataComponents.COMPONENTS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }


}
