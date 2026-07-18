package net.diyarnagibaster.huntrio.research;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ResearchRecipe implements Recipe<ResearchRecipeInput> {
    private final NonNullList<Ingredient> ingredients;
    private final Ingredient reagent;
    private final ItemStack result;

    public ResearchRecipe(NonNullList<Ingredient> ingredients, Ingredient reagent, ItemStack result) {
        this.ingredients = ingredients;
        this.reagent = reagent;
        this.result = result;
    }

    @Override
    public boolean matches(ResearchRecipeInput input, Level level) {
        if (level.isClientSide()) return false;

        // 1. Проверяем, правильным ли реагентом кликнул игрок
        if (!reagent.test(input.reagent())) return false;

        // 2. Проверяем предметы на столе (порядок не имеет значения)
        boolean matchOption1 = ingredients.get(0).test(input.slot1()) && ingredients.get(1).test(input.slot2());
        boolean matchOption2 = ingredients.get(0).test(input.slot2()) && ingredients.get(1).test(input.slot1());

        return matchOption1 || matchOption2;
    }

    @Override
    public ItemStack assemble(ResearchRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RESEARCH_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.RESEARCH_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ResearchRecipe> {

        // Чтение/Запись из JSON
        public static final MapCodec<ResearchRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(r -> r.ingredients), // Здесь мы получаем обычный List
                Ingredient.CODEC_NONEMPTY.fieldOf("reagent").forGetter(r -> r.reagent),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(inst, (ingredients, reagent, result) -> {
            NonNullList<Ingredient> nonNullIngredients = NonNullList.create();
            nonNullIngredients.addAll(ingredients);

            return new ResearchRecipe(nonNullIngredients, reagent, result);
        }));

        // Чтение/Запись по сети (от сервера клиенту)
        public static final StreamCodec<RegistryFriendlyByteBuf, ResearchRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)), r -> r.ingredients,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.reagent,
                ItemStack.STREAM_CODEC, r -> r.result,
                ResearchRecipe::new
        );

        @Override
        public MapCodec<ResearchRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ResearchRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
