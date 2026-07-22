package net.diyarnagibaster.huntrio.blocks;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
                            .requiresCorrectToolForDrops().sound(SoundType.STONE)));



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
