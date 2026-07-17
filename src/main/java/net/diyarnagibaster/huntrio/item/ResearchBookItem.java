package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.research.ResearchJournalScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ResearchBookItem extends Item {

    public ResearchBookItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide())
            openScreen();
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    private void openScreen() {
        net.minecraft.client.Minecraft.getInstance().setScreen(new ResearchJournalScreen());
    }
}
