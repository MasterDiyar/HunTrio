package net.diyarnagibaster.huntrio.enchantments;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> KNOWLEDGE_OF_AGES = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(HunTrio.MODID, "knowledge_of_ages")
    );
}