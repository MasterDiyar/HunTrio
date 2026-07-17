package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Random;

@EventBusSubscriber(modid = HunTrio.MODID)
public class MobDropHandler {

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onMobDrops(LivingDropsEvent event) {

        if (!(event.getEntity() instanceof Strider strider))
            return;

        Level level = strider.level();
        if (level.isClientSide())
            return;

        if (RANDOM.nextInt(100) >= 25)
            return;

        int lootingLevel = 0;

        if (event.getSource().getEntity() instanceof LivingEntity killer) {
            var lootingEnchantment = level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.LOOTING);
            lootingLevel = EnchantmentHelper.getTagEnchantmentLevel(lootingEnchantment, killer.getMainHandItem());
        }

        int amount = 1;
        if (lootingLevel > 0) {
            amount += RANDOM.nextInt(lootingLevel + 1);
        }

        ItemStack silk = new ItemStack(ModItems.NETHER_SILK.get(), amount);

        ItemEntity drop = new ItemEntity(
                level,
                strider.getX(),
                strider.getY(),
                strider.getZ(),
                silk
        );

        event.getDrops().add(drop);
    }
}