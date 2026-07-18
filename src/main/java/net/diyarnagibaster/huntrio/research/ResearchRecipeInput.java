package net.diyarnagibaster.huntrio.research;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ResearchRecipeInput(ItemStack slot1, ItemStack slot2, ItemStack reagent) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> slot1;
            case 1 -> slot2;
            case 2 -> reagent;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 3;
    }
}