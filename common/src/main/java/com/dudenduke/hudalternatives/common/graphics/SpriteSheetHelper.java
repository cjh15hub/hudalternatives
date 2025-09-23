package com.dudenduke.hudalternatives.common.graphics;

import com.dudenduke.hudalternatives.common.numerics.Dimensions;
import com.dudenduke.hudalternatives.common.numerics.Vector2;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpriteSheetHelper {

    public SpriteSheetHelper(ResourceLocation spriteSheet, Dimensions spriteSheetDimensions) {
        SpriteSheet = spriteSheet;
        SheetDimensions = spriteSheetDimensions;
    }

    private final ResourceLocation SpriteSheet;
    private final Dimensions SheetDimensions;

    public enum FillDirection {
        LeftToRight,
        RightToLeft,
        TopToBottom,
        BottomToTop
    }

    /**
     * Helper Method to blit
     * @param guiGraphics graphics instance
     * @param screenX screen x point to draw sprite
     * @param screenY screen y point to draw sprite
     * @param sprite sprite to render from sprite sheet
     */
    public void blitSprite(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite) {
        // argument order has changed across versions
        // BEWARE guiGraphics.blit(...) overloads DO NOT accept params x, y, u, v, etc. in the same order!
        // Could not figure out issues with guiGraphics.blitSprite(...)

        guiGraphics.blit(
            SpriteSheet,
            screenX,
            screenY,
            sprite.u(),
            sprite.v(),
            sprite.width(),
            sprite.height(),
            SheetDimensions.width(),
            SheetDimensions.height()
        );
    }

    /**
     * Helper method to blit
     * @param guiGraphics graphics instance
     * @param point screen point to draw sprite
     * @param sprite sprite to render from SpriteSheet
     */
    public void blitSprite(GuiGraphics guiGraphics, Vector2 point, Sprite sprite) {
        blitSprite(guiGraphics, point.x(), point.y(), sprite);
    }

    /**
     * Helper method to blit sprite partials to be used like a meter
     * @param guiGraphics graphics instance
     * @param point screen point to draw sprite (as if it were full)
     * @param sprite sprite to render from SpriteSheet
     * @param valuePercent a number 0 -> 1 indicating the percent of the sprite to render
     * @param fillDirection the direction the sprite should fill in LowToHigh
     */
    public void blitMeter(GuiGraphics guiGraphics, Vector2 point, Sprite sprite, float valuePercent, FillDirection fillDirection) {
        blitMeter(guiGraphics, point.x(), point.y(), sprite, valuePercent, fillDirection);
    }

    /**
     * Helper method to blit sprite partials to be used like a meter
     * @param guiGraphics graphics instance
     * @param screenX screen x point to draw sprite (as if it were full)
     * @param screenY screen y point to draw sprite (as if it were full)
     * @param sprite sprite to render from SpriteSheet
     * @param valuePercent a number 0 -> 1 indicating the percent of the sprite to render
     * @param fillDirection the direction the sprite should fill in LowToHigh
     */
    public void blitMeter(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite, float valuePercent, FillDirection fillDirection) {
        // MAKE SURE EACH OF THESE MATCH THE INTERNAL STRUCTURE OF blitSprite(...)
        switch (fillDirection) {
            case LeftToRight -> blitMeterLeftToRight(guiGraphics, screenX, screenY, sprite, valuePercent);
            case RightToLeft -> blitMeterRightToLeft(guiGraphics, screenX, screenY, sprite, valuePercent);
            case TopToBottom -> blitMeterTopToBottom(guiGraphics, screenX, screenY, sprite, valuePercent);
            case BottomToTop -> blitMeterBottomToTop(guiGraphics, screenX, screenY, sprite, valuePercent);
        }
    }

    private void blitMeterLeftToRight(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite, float valuePercent) {
        final int renderedWidth = ((int)(sprite.width() * valuePercent));

        guiGraphics.blit(
            SpriteSheet,
            screenX,
            screenY,
            sprite.u(),
            sprite.v(),
            renderedWidth,
            sprite.height(),
            SheetDimensions.width(),
            SheetDimensions.height()
        );
    }

    private void blitMeterRightToLeft(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite, float valuePercent) {
        int removedSpriteWidth = sprite.width() - ((int)(sprite.width() * valuePercent));
        int spriteStartU = removedSpriteWidth + sprite.u();
        int spriteWidthRemaining = sprite.width() - removedSpriteWidth;

        guiGraphics.blit(
            SpriteSheet,
            screenX + removedSpriteWidth,
            screenY,
            spriteStartU,
            sprite.v(),
            spriteWidthRemaining,
            sprite.height(),
            SheetDimensions.width(),
            SheetDimensions.height()
        );
    }

    private void blitMeterTopToBottom(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite, float valuePercent) {
        final int renderedHeight = ((int)(sprite.height() * valuePercent));

        guiGraphics.blit(
            SpriteSheet,
            screenX,
            screenY,
            sprite.u(),
            sprite.v(),
            sprite.width(),
            renderedHeight,
            SheetDimensions.width(),
            SheetDimensions.height()
        );
    }

    private void blitMeterBottomToTop(GuiGraphics guiGraphics, int screenX, int screenY, Sprite sprite, float valuePercent) {
        int removedSpriteHeight = sprite.height() - ((int)(sprite.height() * valuePercent));
        int spriteStartV = removedSpriteHeight + sprite.v();
        int spriteHeightRemaining = sprite.height() - removedSpriteHeight;

        guiGraphics.blit(
            SpriteSheet,
            screenX,
            screenY + removedSpriteHeight,
            sprite.u(),
            spriteStartV,
            sprite.width(),
            spriteHeightRemaining,
            SheetDimensions.width(),
            SheetDimensions.height()
        );
    }
}
