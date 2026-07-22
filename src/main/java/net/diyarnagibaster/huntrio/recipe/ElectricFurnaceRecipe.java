package net.diyarnagibaster.huntrio.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record ElectricFurnaceRecipe(Ingredient ingredient1, Ingredient ingredient2,
                                    ItemStack output1, ItemStack output2,
                                    int energyCost, int processingTime) implements Recipe<ElectricFurnaceInput> {

    @Override
    public boolean matches(ElectricFurnaceInput input, Level level) {
        boolean match1 = ingredient1.test(input.getItem(0)) && ingredient2.test(input.getItem(1));
        boolean match2 = ingredient1.test(input.getItem(1)) && ingredient2.test(input.getItem(0));
        return match1 || match2;
    }

    @Override
    public ItemStack assemble(ElectricFurnaceInput input, HolderLookup.Provider provider) {
        return output1.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ElectricFurnaceRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "electric_smelting";
    }

    public static class Serializer implements RecipeSerializer<ElectricFurnaceRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<ElectricFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(ElectricFurnaceRecipe::ingredient1),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(ElectricFurnaceRecipe::ingredient2),
                ItemStack.CODEC.fieldOf("output1").forGetter(ElectricFurnaceRecipe::output1),
                ItemStack.OPTIONAL_CODEC.optionalFieldOf("output2", ItemStack.EMPTY).forGetter(ElectricFurnaceRecipe::output2),
                Codec.INT.optionalFieldOf("energy", 1000).forGetter(ElectricFurnaceRecipe::energyCost),
                Codec.INT.optionalFieldOf("time", 200).forGetter(ElectricFurnaceRecipe::processingTime)
        ).apply(inst, ElectricFurnaceRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ElectricFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ElectricFurnaceRecipe::ingredient1,
                Ingredient.CONTENTS_STREAM_CODEC, ElectricFurnaceRecipe::ingredient2,
                ItemStack.STREAM_CODEC, ElectricFurnaceRecipe::output1,
                ItemStack.OPTIONAL_STREAM_CODEC, ElectricFurnaceRecipe::output2,
                ByteBufCodecs.INT, ElectricFurnaceRecipe::energyCost,
                ByteBufCodecs.INT, ElectricFurnaceRecipe::processingTime,
                ElectricFurnaceRecipe::new
        );

        @Override public MapCodec<ElectricFurnaceRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, ElectricFurnaceRecipe> streamCodec() { return STREAM_CODEC; }
    }
}