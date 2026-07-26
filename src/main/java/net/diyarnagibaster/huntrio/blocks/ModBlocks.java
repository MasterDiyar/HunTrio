package net.diyarnagibaster.huntrio.blocks;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HunTrio.MODID);

    public static final DeferredBlock<Block>
            ALUMINIUM_BLOCK = registerBlock("aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))),
            RESEARCH_TABLE = registerBlock("research_table",
                    () -> new ResearchTableBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())),
            DESICCANT = registerBlock("desiccant",
            () -> new Desiccant(BlockBehaviour.Properties.of().strength(3.0f).noOcclusion().
                    requiresCorrectToolForDrops().sound(SoundType.LODESTONE))),
            ELECTRIC_FURNACE = registerBlock("electro_furnace",
                    () -> new ElectricFurnaceBlock(BlockBehaviour.Properties.of().strength(2f)
                            .requiresCorrectToolForDrops().sound(SoundType.STONE))),
            SOLONETS = registerBlock("solonets", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT))),
            SOLONETS_GRASS_BLOCK = registerBlock("solonets_grass_block", () -> new SolonetsGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK))),
            CICHORIUM = registerBlock("cichorium", () -> new FlowerBlock(
                    MobEffects.SATURATION, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)
            )),
            LITHIUM_TNT = registerBlock("lithium_tnt",
                    () -> new CustomTntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
