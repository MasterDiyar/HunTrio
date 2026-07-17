package net.diyarnagibaster.huntrio.item;

import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;

public class CustomBrushItem extends BrushItem {
    private final int baseDropChance;

    public CustomBrushItem(Properties properties, int baseDropChance) {
        super(properties);
        this.baseDropChance = baseDropChance;
    }

    public int getBaseDropChance() {
        return this.baseDropChance;
    }
}