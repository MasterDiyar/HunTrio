package net.diyarnagibaster.huntrio.entity;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HunTrio.MODID);

    public static final Supplier<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE_BE =
            BLOCK_ENTITIES.register("research_table_be", () ->
                    BlockEntityType.Builder.of(
                            ResearchTableBlockEntity::new,
                            ModBlocks.RESEARCH_TABLE.get()
                    ).build(null));
    public static final Supplier<BlockEntityType<ElectricFurnaceBlockEntity>>
            ELECTRIC_FURNACE = BLOCK_ENTITIES.register("electric_furnace_be", ()-> BlockEntityType.Builder.of(
                    ElectricFurnaceBlockEntity::new,
                    ModBlocks.ELECTRIC_FURNACE.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);

    }
}