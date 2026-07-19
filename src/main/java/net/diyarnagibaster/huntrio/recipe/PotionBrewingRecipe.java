package net.diyarnagibaster.huntrio.recipe;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class PotionBrewingRecipe implements IBrewingRecipe {

    private final Holder<Potion> inputPotion;
    private final Item ingredient;
    private final ItemStack output;

    public PotionBrewingRecipe(Holder<Potion> inputPotion, Item ingredient, ItemStack output) {
        this.inputPotion = inputPotion;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack inputStack) {
        if (!inputStack.is(Items.POTION)) {
            return false;
        }

        PotionContents contents = inputStack.get(DataComponents.POTION_CONTENTS);

        return contents != null && contents.is(this.inputPotion);
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
