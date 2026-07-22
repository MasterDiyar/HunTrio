package net.diyarnagibaster.huntrio.research;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.recipe.ElectricFurnaceRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, HunTrio.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, HunTrio.MODID);

    public static final Supplier<RecipeType<ElectricFurnaceRecipe>> ELECTRIC_SMELTING_TYPE =
            TYPES.register("electric_smelting", () -> ElectricFurnaceRecipe.Type.INSTANCE);

    public static final Supplier<RecipeSerializer<ElectricFurnaceRecipe>> ELECTRIC_SMELTING_SERIALIZER =
            SERIALIZERS.register("electric_smelting", () -> ElectricFurnaceRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<ResearchRecipe>> RESEARCH_SERIALIZER =
            SERIALIZERS.register("research", ResearchRecipe.Serializer::new);

    public static final Supplier<RecipeType<ResearchRecipe>> RESEARCH_TYPE =
            TYPES.register("research", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "research";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}