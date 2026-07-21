package net.diyarnagibaster.huntrio.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectricFurnaceScreen extends AbstractContainerScreen<ElectricFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HunTrio.MODID, "textures/gui/electric_furnace_gui.png");

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 1. Рисуем основной фон GUI
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 2. Рисуем полоску энергии
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        if (maxEnergy > 0 && energy > 0) {
            // Допустим, высота твоей полоски энергии 50 пикселей.
            // На текстуре она нарисована справа от основного интерфейса (например, x=256, y=0)
            int scaledHeight = (int) (((float) energy / maxEnergy) * 50);

            // Координаты: x_на_экране, y_на_экране, x_на_текстуре, y_на_текстуре, ширина, высота
            graphics.blit(TEXTURE, x + 8, y + 53 - scaledHeight, 256, 50 - scaledHeight, 16, scaledHeight);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY); // Включаем подсказки предметов
    }
}