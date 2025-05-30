package grill24.learningtheropes.gui;

import grill24.learningtheropes.LearningTheRopes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(
            LearningTheRopes.MOD_ID, "textures/gui/solar_panel.png");
    private static final Point2D.Float[] METER_FRAME_POSITIONS = getMeterFramePositions();
    private float frame;

    public SolarPanelScreen(SolarPanelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    private static Point2D.Float[] getMeterFramePositions() {
        int x = 176;
        int y = 0;

        final int energyBarHeight = 69;
        final int energyBarWidth = 16;

        final int textureWidth = 256;
        final int textureHeight = 256;

        final int frames = 6;
        final int framesPerRow = (textureWidth - x) / energyBarWidth;

        Point2D.Float[] positions = new Point2D.Float[frames];
        for (int i = 0; i < frames; i++) {
            positions[i] = new Point2D.Float(
                    x  + (i % framesPerRow * energyBarWidth),
                    y + (i / framesPerRow * energyBarHeight)
            );
        }

        return positions;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int i, int i1) {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        // Render the background of the solar panel GUI
        graphics.blit(
                RenderType::guiTextured,
                BACKGROUND_LOCATION,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256
        );

        // Blit energy storage bar
        int energy = this.menu.getEnergyStorage();
        int energyBarHeight = 69;
        int energyBarWidth = 16;
        int filledHeight = (int) ((energy / 100F) * energyBarHeight);

        int rate = this.menu.getLastEnergyDelta();

        final float framesPerSecond = Math.clamp(rate, -10, 10); // Adjust this value to control the speed of the animation
        final float ticksPerFrame = 20F / framesPerSecond; // 20 ticks per second
        frame = (frame + partialTick / ticksPerFrame) % METER_FRAME_POSITIONS.length;
        if (frame < 0) {
            frame += METER_FRAME_POSITIONS.length; // Ensure frame is non-negative
        }
        Point2D.Float framePosition = METER_FRAME_POSITIONS[(int) frame];
        System.out.println("Frame Index: " + frame + ", Frame Position: " + framePosition);

        graphics.blit(
                RenderType::guiTextured,
                BACKGROUND_LOCATION,
                    this.leftPos + 152, this.topPos + 8 + (energyBarHeight - filledHeight),
                (int) framePosition.getX(), (int) framePosition.getY() + energyBarHeight - filledHeight,
                energyBarWidth, filledHeight,
                256, 256
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int color = ARGB.white(1F);
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color, true);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color, true);
    }
}
