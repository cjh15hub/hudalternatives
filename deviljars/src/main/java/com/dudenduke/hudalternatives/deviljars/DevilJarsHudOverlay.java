package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.common.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class DevilJarsHudOverlay {

    public static final ResourceLocation DEVIL_JARS = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/ender_golem_jars_sprite_sheet.png");
    private static final SpriteSheetHelper _spriteSheetHelper = new SpriteSheetHelper(DEVIL_JARS, DJ_Sprites.FullSheet.dimensions());

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        final Dimensions screenDims = new Dimensions(screenWidth, screenHeight);

        SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);

        Vector2 leftScreenAnchor = getLeftScreenAnchorPoint(screenDims);
        Vector2 rightScreenAnchor = getRightScreenAnchorPoint(screenDims);

        renderHealthJar(guiGraphics, leftScreenAnchor, playerSnapshot.health, playerSnapshot.maxHealth, playerSnapshot.healthEffect);
        renderDragon(guiGraphics, leftScreenAnchor);

        renderIronGolem(guiGraphics, rightScreenAnchor);
        renderManaJar(guiGraphics, rightScreenAnchor, playerSnapshot.foodLevel, playerSnapshot.maxFoodLevel, playerSnapshot.hungerEffect);

    }

    private static Vector2 getLeftScreenAnchorPoint(Dimensions screen) {
        return new Vector2(
            (screen.width() / 2) - 92 - DJ_Sprites.EmptyJar.width(),
            screen.height() - DJ_Sprites.EmptyJar.height()
        );
    }

    private static Vector2 getRightScreenAnchorPoint(Dimensions screen) {
        return new Vector2(
            (screen.width() / 2) + 92,
            screen.height() - DJ_Sprites.EmptyJar.height()
        );
    }

    private static void renderHealthJar(GuiGraphics guiGraphics, Vector2 anchor, float health, float maxHealth, SurvivalPlayerSnapshot.Effect healthState) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x(),
            anchor.y(),
            DJ_Sprites.EmptyJar
        );
        // TODO: update to get correct sprite based on SurvivalPlayerSnapshot.Effect(s)
        Sprite healthBarSprite = DJ_Sprites.MainHealthBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            anchor.x() + 4,
            anchor.y() + 5,
            healthBarSprite,
            (health / maxHealth),
            SpriteSheetHelper.FillDirection.BottomToTop
        );
    }

    private static void renderManaJar(GuiGraphics guiGraphics, Vector2 anchor, float foodLevel, float maxFoodLevel, SurvivalPlayerSnapshot.Effect hungerState) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x(),
            anchor.y(),
            DJ_Sprites.EmptyJar
        );
        // TODO: update to get correct sprite based on SurvivalPlayerSnapshot.Effect(s)
        Sprite manaBarSprite = DJ_Sprites.MainManaBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            anchor.x() + 4,
            anchor.y() + 5,
            manaBarSprite,
            (foodLevel / maxFoodLevel),
            SpriteSheetHelper.FillDirection.BottomToTop
        );
    }

    private static void renderDragon(GuiGraphics guiGraphics, Vector2 anchor) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() - DJ_Sprites.Dragon.width() + 8,
            anchor.y() - 4,
            DJ_Sprites.Dragon
        );
    }

    private static void renderIronGolem(GuiGraphics guiGraphics, Vector2 anchor) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() + DJ_Sprites.EmptyJar.width() - 6,
            anchor.y() - 10,
            DJ_Sprites.Golem
        );
    }
}
