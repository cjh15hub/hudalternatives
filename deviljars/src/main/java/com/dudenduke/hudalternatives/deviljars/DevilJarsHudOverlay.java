package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.common.numerics.Vector2;
import com.dudenduke.hudalternatives.common.numerics.Dimensions;
import com.dudenduke.hudalternatives.common.graphics.Sprite;
import com.dudenduke.hudalternatives.common.graphics.SpriteSheetHelper;
import com.dudenduke.hudalternatives.common.player.SurvivalPlayerSnapshot;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class DevilJarsHudOverlay {

    public static final ResourceLocation DEVIL_JARS = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/ender_golem_jars_sprite_sheet.png");
    private static final SpriteSheetHelper _spriteSheetHelper = new SpriteSheetHelper(DEVIL_JARS, DJ_Sprites.FullSheet.dimensions());

    public static void emptyRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        return;
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        final Dimensions screenDims = new Dimensions(screenWidth, screenHeight);

        SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);

        Vector2 leftScreenAnchor = getLeftScreenAnchorPoint(screenDims);
        Vector2 rightScreenAnchor = getRightScreenAnchorPoint(screenDims);

        renderHotbarConnector(guiGraphics, screenDims);
        renderHealthJar(guiGraphics, leftScreenAnchor, playerSnapshot.health, playerSnapshot.maxHealth, playerSnapshot.healthEffect);
        renderGoldenHealthBar(guiGraphics, leftScreenAnchor, playerSnapshot.absorption);
        renderDragon(guiGraphics, leftScreenAnchor);

        renderIronGolem(guiGraphics, rightScreenAnchor);
        renderManaJar(guiGraphics, rightScreenAnchor, playerSnapshot.foodLevel, playerSnapshot.maxFoodLevel, playerSnapshot.hungerEffect);
        renderSaturationBar(guiGraphics, rightScreenAnchor, playerSnapshot.saturation, playerSnapshot.maxFoodLevel);
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

        Sprite healthBarSprite = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? DJ_Sprites.WitheredHealthBar
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? DJ_Sprites.PoisonedHealthBar
            : DJ_Sprites.MainHealthBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            anchor.x() + 4,
            anchor.y() + 5,
            healthBarSprite,
            (health / maxHealth),
            SpriteSheetHelper.FillDirection.BottomToTop
        );
    }

    private static void renderGoldenHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float absorption) {
        float maxAbsorption = 18f;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 4,
            mainAnchor.y() + 5,
            DJ_Sprites.GoldenHealthBar,
            (absorption / maxAbsorption),
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

        Sprite manaBarSprite = (hungerState == SurvivalPlayerSnapshot.Effect.HUNGERED)
            ? DJ_Sprites.HungeredManaBar
            : DJ_Sprites.MainManaBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            anchor.x() + 4,
            anchor.y() + 5,
            manaBarSprite,
            (foodLevel / maxFoodLevel),
            SpriteSheetHelper.FillDirection.BottomToTop
        );
    }

    private static void renderSaturationBar(GuiGraphics guiGraphics, Vector2 anchor, float saturation, float maxFoodLevel) {
        _spriteSheetHelper.blitMeter(
            guiGraphics,
            anchor.x() + 4,
            anchor.y() + 5,
            DJ_Sprites.GoldenHealthBar,
            (saturation / maxFoodLevel),
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

    private static void renderHotbarConnector(GuiGraphics guiGraphics, Dimensions screenDims) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            (screenDims.width() / 2) - (DJ_Sprites.HotbarConnector.width() / 2),
            screenDims.height() - (DJ_Sprites.HotbarConnector.height()),
            DJ_Sprites.HotbarConnector
        );
    }
}
