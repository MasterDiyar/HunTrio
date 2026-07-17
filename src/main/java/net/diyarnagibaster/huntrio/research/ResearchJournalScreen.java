package net.diyarnagibaster.huntrio.research;

import net.diyarnagibaster.huntrio.server.ClientResearchData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ResearchJournalScreen extends Screen {
    private static final ResourceLocation BG_TEXTURE = ResourceLocation.fromNamespaceAndPath("huntrio", "textures/gui/research_book.png");

    private final int imageWidth = 256;
    private final int imageHeight = 180;
    private int leftPos;
    private int topPos;

    public ResearchJournalScreen() {
        super(Component.literal("Журнал Исследований"));
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("X"),
                        button -> this.onClose())
                .bounds(this.leftPos + 230, this.topPos + 10, 15, 15).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.blit(BG_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (ClientResearchData.isUnlocked("manganese_study")) {
            guiGraphics.drawString(this.font, "Марганец: Обнаружена кристаллическая структура...", this.leftPos + 20, this.topPos + 30, 0x404040, false);
        } else {
            guiGraphics.drawString(this.font, "Заблокировано. Изучите мир кистью.", this.leftPos + 20, this.topPos + 30, 0x888888, false);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}