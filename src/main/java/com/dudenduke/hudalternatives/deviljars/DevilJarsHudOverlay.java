package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.Constants;
import com.dudenduke.hudalternatives.utils.Dimensions;
import com.dudenduke.hudalternatives.utils.SurvivalPlayerSnapshot;
import com.dudenduke.hudalternatives.utils.Vector2;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DevilJarsHudOverlay {

    private static final ResourceLocation DEVIL_JARS = new ResourceLocation(Constants.MODID, "textures/devil_jars_sprite_sheet.png");

    private static final int spriteSheetTextureWidth = 256;
    private static final int spriteSheetTextureHeight = 256;
    private static final float maxHealth = 20f;
    private static final float maxFoodLevel = 20f;
    private static final int relativeJarTop = 37;
    private static final int jarPadding = 3;


    public static final IGuiOverlay DEVIL_JARS_HUD = new IGuiOverlay() {
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            // get variables for sprites
            final LocalPlayer player = gui.getMinecraft().player;
            if (player == null) return;

            final var screenDims = new Dimensions(screenWidth, screenHeight);
            SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);

            // render sprites
            var topperCords = renderHotbarTopperSprite(guiGraphics, screenWidth, screenHeight);

            var dragonJarDestination = getDragonJarSpriteDestination(topperCords.getFirst());
            // renderDragonAuraSprite(guiGraphics, screenWidth, screenHeight, dragonJarDestination);
            renderRoundHealthSprite(guiGraphics, screenWidth, screenHeight, dragonJarDestination, playerSnapshot.health, playerSnapshot.healthEffect);
            if (playerSnapshot.absorption > 0) {
                renderRoundGoldenHealthSprite(guiGraphics, screenWidth, screenHeight, dragonJarDestination, playerSnapshot.absorption);
            }
            renderDragonJarSprite(guiGraphics, screenWidth, screenHeight, dragonJarDestination);

            renderHungerBarSprite(guiGraphics, screenWidth, screenHeight, playerSnapshot.foodLevel, playerSnapshot.hungerEffect);
            renderSaturationSprite(guiGraphics, screenWidth, screenHeight, playerSnapshot.saturation);
            renderHungerFrameSprite(guiGraphics, screenWidth, screenHeight);

            // renderHealthBarSprite(guiGraphics, screenWidth, screenHeight, health, healthState);
            //renderBackgroundSprite(guiGraphics, screenWidth, screenHeight);
        }

    };

    private static void renderBackgroundSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        final int halfScreen = screenWidth / 2;

        final int spriteLocationY = 216;
        final int spriteWidth = 256;
        final int spriteHeight = 40;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2),
            screenHeight - spriteHeight,
            0, spriteLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static Pair<Vector2, Vector2> renderHotbarTopperSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        final int halfScreen = screenWidth / 2;

        final int spriteLocationX = 38;
        final int spriteLocationY = 231;
        final int spriteWidth = 180;
        final int spriteHeight = 6;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteWidth / 2),
            screenHeight - (spriteSheetTextureHeight - spriteLocationY),
            spriteLocationX, spriteLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );

        return new Pair<>(
            new Vector2(spriteLocationX, spriteLocationY),
            new Vector2(spriteLocationX + spriteWidth, spriteLocationY + spriteHeight)
        );
    }

    private static Vector2 getDragonJarSpriteDestination(Vector2 hotbarLeft) {
        final int spriteDrawLocationX = hotbarLeft.x() - 54; // 54 left offset from hotbar
        final int spriteDrawLocationY = 209;
        return new Vector2(spriteDrawLocationX, spriteDrawLocationY);
    }

    private static void renderDragonJarSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Vector2 destination) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 0;
        final int spriteSourceLocationY = 0;

        final int spriteWidth = 54;
        final int spriteHeight = 46;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2) + destination.x(),
            screenHeight - (spriteSheetTextureHeight - destination.y()),
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderDragonAuraSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Vector2 dragonJarLeft) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 0;
        final int spriteSourceLocationY = 48;

        final int spriteDrawLocationX = dragonJarLeft.x() -2; // 2 left offset dragon
        final int spriteDrawLocationY = dragonJarLeft.y() - 2; // 2 up from dragon
        final int spriteWidth = 59;
        final int spriteHeight = 51;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2) + spriteDrawLocationX,
            screenHeight - (spriteSheetTextureHeight - spriteDrawLocationY),
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderRoundHealthSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Vector2 dragonJarLeft, float health, SurvivalPlayerSnapshot.Effect healthState) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? 121
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? 88
            : 55;
        final int spriteSourceLocationY = 9;

        final int spriteDrawLocationX = dragonJarLeft.x() + 19; // 19 offset from dragon
        final int spriteDrawLocationY = dragonJarLeft.y() + 9; // 9 offset from dragon
        final int spriteWidth = 32;
        final int spriteHeight = 32;

        int removedSpriteHeight = spriteHeight - ((int)(spriteHeight * (health / maxHealth)));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = spriteHeight - removedSpriteHeight;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2) + spriteDrawLocationX,
            screenHeight - (spriteSheetTextureHeight - spriteDrawLocationY) + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            spriteWidth, spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderRoundGoldenHealthSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Vector2 dragonJarLeft, float absorption) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 154;
        final int spriteSourceLocationY = 9;

        final int spriteDrawLocationX = dragonJarLeft.x() + 19; // 19 offset from dragon
        final int spriteDrawLocationY = dragonJarLeft.y() + 9; // 9 offset from dragon
        final int spriteWidth = 32;
        final int spriteHeight = 32;

        final float absorptionOffset = 2f;
        float maxAbsorption = 16f;
        if (absorption < 5f) {
            absorption += absorptionOffset;
            maxAbsorption += absorptionOffset;
        }
        int removedSpriteHeight = spriteHeight - ((int)(spriteHeight * (absorption / maxAbsorption)));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = spriteHeight - removedSpriteHeight;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2) + spriteDrawLocationX,
            screenHeight - (spriteSheetTextureHeight - spriteDrawLocationY) + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            spriteWidth, spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderHealthBarSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float health, SurvivalPlayerSnapshot.Effect healthState) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 3;
        final int spriteSourceLocationY = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? 117
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? 150
            : 183;

        final int spriteWidth = 32;
        final int spriteHeight = 32;

        int removedSpriteHeight = spriteHeight - ((int)(spriteHeight * (health / maxHealth)));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = spriteHeight - removedSpriteHeight;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen - (spriteSheetTextureWidth / 2) + jarPadding,
            screenHeight - relativeJarTop + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            spriteWidth, spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderHungerFrameSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 218;
        final int spriteSourceLocationY = 216;
        final int spriteWidth = 38;
        final int spriteHeight = 40;

        final int spriteLocationX = 32;
        final int spriteLocationY = 32;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen + (spriteSheetTextureWidth / 2) - spriteWidth,
            screenHeight - spriteHeight,
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderHungerBarSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float foodLevel, SurvivalPlayerSnapshot.Effect hungerState) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 221;
        final int spriteSourceLocationY = (hungerState == SurvivalPlayerSnapshot.Effect.HUNGERED) ? 150 : 183;
        final int spriteWidth = 32;
        final int spriteHeight = 32;

        int removedSpriteHeight = spriteHeight - ((int)(spriteHeight * (foodLevel / maxFoodLevel)));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = spriteHeight - removedSpriteHeight;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen + (spriteSheetTextureWidth / 2) - spriteWidth - jarPadding,
            screenHeight - relativeJarTop + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            spriteWidth, spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderSaturationSprite(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float saturation) {
        final int halfScreen = screenWidth / 2;

        final int spriteSourceLocationX = 221;
        final int spriteSourceLocationY = 117;
        final int spriteWidth = 32;
        final int spriteHeight = 32;

        int removedSpriteHeight = spriteHeight - ((int)(spriteHeight * (saturation / maxFoodLevel)));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = spriteHeight - removedSpriteHeight;

        guiGraphics.blit(
            DEVIL_JARS,
            halfScreen + (spriteSheetTextureWidth / 2) - spriteWidth - jarPadding,
            screenHeight - relativeJarTop + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            spriteWidth, spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

}

