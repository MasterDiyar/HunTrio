package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.diyarnagibaster.huntrio.recipe.CustomBrewingRecipe;
import net.diyarnagibaster.huntrio.recipe.PotionBrewingRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = HunTrio.MODID)
public class BrewingRecipeHandler {

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

       builder.addRecipe(new PotionBrewingRecipe(
                Potions.WEAKNESS,
                Items.LAPIS_LAZULI,
                new ItemStack(ModItems.SULFUR_JAR.get(), 1)
                )
        );

        builder.addRecipe(new PotionBrewingRecipe(
                        Potions.WATER,
                        Items.KELP,
                        new ItemStack(ModItems.SALT_JAR.get(), 1)
                )
        );



    }
}