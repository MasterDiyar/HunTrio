package net.diyarnagibaster.huntrio.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class CustomBrewingRecipe implements IBrewingRecipe {

    private final Item input;
    private final Item ingredient;
    private final ItemStack output;

    public CustomBrewingRecipe(Item input, Item ingredient, ItemStack output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack inputStack) {
        return inputStack.is(this.input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredientStack) {
        return ingredientStack.is(this.ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack inputStack, ItemStack ingredientStack) {
        if (isInput(inputStack) && isIngredient(ingredientStack)) {
            return this.output.copy();
        }

        return ItemStack.EMPTY;
    }
}
