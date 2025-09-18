package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.common.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class MinimalModernOverlay {
    public static final ResourceLocation MINIMAL_MODERN = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/minimal_modern.png");
    private static final SpriteSheetHelper _spriteSheetHelper = new SpriteSheetHelper(MINIMAL_MODERN, MM_Sprites.FullSheet.dimensions());

    private static final Duration horseHpNumbersSolidSeconds = Duration.ofMillis(1500);
    private static final Duration horseHpNumbersFadeSeconds = Duration.ofMillis(1500);
    private static final Duration horseHpNumbersShowTime = horseHpNumbersSolidSeconds.plus(horseHpNumbersFadeSeconds);

    private static final Font DefaultFont = Minecraft.getInstance().font;
    private static final Color BrightGreen = new Color(0, 233, 104);
    private static final Color SteelBlue = new Color(0, 141, 184);
    private static final Color BrightOrange = new Color(214, 84, 16);
    private static final Color MediumGray = new Color(92, 92, 92);
    private static final int threeBytes = 24;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        final Dimensions screenDims = new Dimensions(screenWidth, screenHeight);
        SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);
        PlayerMountData.updateMountData(player);

        // Standard reference point for all gui elements
        Vector2 mainScreenAnchor = getMainScreenAnchorPoint(screenDims, MM_Configuration.MainGuiDrawCorner());
        Vector2 valueBarAnchorPoint = mainScreenAnchor;  // if separated, adjusted anchor point for value bars

        // Main Elements and Value Bars Separated
        boolean separatedBars = (MM_Configuration.SeparateValueBars() && MM_Configuration.ValueBarsDrawCorner() != MM_Configuration.MainGuiDrawCorner());
        if (separatedBars) {
            // Main GUI rendered left side
            if (MM_Configuration.MainGuiDrawCorner() == 1 || MM_Configuration.MainGuiDrawCorner() == 3) {
                int adjustedX = mainScreenAnchor.x() + MM_Sprites.ValueBarsBackground.width() - (MM_Sprites.SmallHex.width() / 2) - 1;
                mainScreenAnchor = new Vector2(adjustedX, mainScreenAnchor.y());
            }
            // Adjust the value bars anchor point
            valueBarAnchorPoint = getSeparatedValueBarsAnchorPoint(screenDims, MM_Configuration.ValueBarsDrawCorner());
            renderValueBarsBackground(guiGraphics, valueBarAnchorPoint);
        }
        else {
            // All Elements Together
            renderValueBarsBackground(guiGraphics, mainScreenAnchor);
        }

        // Main GUI Elements
        renderMainHex(guiGraphics, mainScreenAnchor);
        renderHotbarHexes(guiGraphics, mainScreenAnchor);
        renderActiveItem(guiGraphics, playerSnapshot, mainScreenAnchor);
        renderAdjacentItems(guiGraphics, playerSnapshot, mainScreenAnchor);

        // Value Bars
        renderHealthBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.health, playerSnapshot.maxHealth, playerSnapshot.healthEffect);
        renderGoldenHealthBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.absorption);
        renderFoodBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.foodLevel, playerSnapshot.maxFoodLevel, playerSnapshot.hungerEffect);
        renderFoodSaturationBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.saturation, playerSnapshot.maxFoodLevel);

        // Misc Survival
        renderDrowningBar(guiGraphics, mainScreenAnchor, playerSnapshot.drownPercentage);

        // Player Riding Mount
        if (PlayerMountData.isPlayerMounted() && PlayerMountData.getMountType() != null) {
            renderMountHex(guiGraphics, mainScreenAnchor, PlayerMountData.getMountType());

            Vector2 mountHealthBarAnchorPoint = (!separatedBars)
                ? mainScreenAnchor
                : getSeparatedValueBarsAnchorPoint(screenDims, MM_Configuration.ValueBarsDrawCorner());
            renderMountHealthBar(guiGraphics, mountHealthBarAnchorPoint, separatedBars, PlayerMountData.getMountHealth(), PlayerMountData.getMountMaxHealth());
        }
        else {
            renderExperienceLevel(guiGraphics, mainScreenAnchor, playerSnapshot.experienceLevel);
        }
    }

    private static Vector2 getMainScreenAnchorPoint(Dimensions screen, int drawCorner) {
        int leftX = 10 +MM_Configuration.MainGuiHorizontalPadding();
        int rightX = screen.width() - MM_Sprites.LargeHex.width() - MM_Sprites.MainHealthBar.width() -2 -MM_Configuration.MainGuiHorizontalPadding();
        int topY = 19 +MM_Configuration.MainGuiVerticalPadding();
        int bottomY = screen.height() - MM_Sprites.LargeHex.height() - 19 -MM_Configuration.MainGuiVerticalPadding();

        return switch(drawCorner) {
            case 0 -> new Vector2(leftX, topY);     // top left
            case 1 -> new Vector2(rightX, topY);    // top right
            case 2 -> new Vector2(leftX, bottomY);  // bottom left
            case 3 -> new Vector2(rightX, bottomY); // bottom right
            default -> new Vector2(leftX, bottomY); // bottom left (default)
        };
    }

    private static Vector2 getSeparatedValueBarsAnchorPoint(Dimensions screen, int drawCorner) {
        int leftX = 4 - MM_Sprites.LargeHex.width() +MM_Configuration.ValueBarsHorizontalPadding();
        int rightX = screen.width() - MM_Sprites.LargeHex.width() - MM_Sprites.MainHealthBar.width() -2 -MM_Configuration.ValueBarsHorizontalPadding();
        int topY = 19 - MM_Sprites.SmallHex.height() +MM_Configuration.ValueBarsVerticalPadding();
        int bottomY = screen.height() - MM_Sprites.LargeHex.height() - 19 + MM_Sprites.SmallHex.height() -MM_Configuration.ValueBarsVerticalPadding();

        return switch(drawCorner) {
            case 0 -> new Vector2(leftX, topY);     // top left
            case 1 -> new Vector2(rightX, topY);    // top right
            case 2 -> new Vector2(leftX, bottomY);  // bottom left
            case 3 -> new Vector2(rightX, bottomY); // bottom right
            default -> new Vector2(leftX, bottomY); // bottom left (default)
        };
    }

    private static void renderMainHex(GuiGraphics guiGraphics, Vector2 anchor) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor,
            MM_Sprites.LargeHex
        );
    }

    private static void renderValueBarsBackground(GuiGraphics guiGraphics, Vector2 anchor) {
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() + 31,
            anchor.y() + 11,
            MM_Sprites.ValueBarsBackground
        );
    }

    private static void renderHotbarHexes(GuiGraphics guiGraphics, Vector2 anchor) {
        // top left
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() - 9,
            anchor.y() - 18,
            MM_Sprites.SmallHex
        );

        // bottom right
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() + 20,
            anchor.y() + 30,
            MM_Sprites.SmallHex
        );
    }

    private static void renderExperienceLevel(GuiGraphics guiGraphics, Vector2 mainAnchor, int experienceLevel) {
        guiGraphics.drawString(DefaultFont, String.valueOf(experienceLevel), mainAnchor.x() + 26, mainAnchor.y() - 3, BrightGreen.getRGB(), true);
    }

    private static void renderActiveItem(GuiGraphics guiGraphics, SurvivalPlayerSnapshot player, Vector2 mainAnchor) {
        final float scaleFactor = 1.5f;

        renderItemWithScale(guiGraphics, player.mainHandItem, mainAnchor.x() + 4, mainAnchor.y() + 6, scaleFactor);

        guiGraphics.renderItemDecorations(DefaultFont, player.mainHandItem, mainAnchor.x() + 8, mainAnchor.y() + 13);

        guiGraphics.drawString(DefaultFont, String.valueOf(player.selectedHotbarIndex + 1), mainAnchor.x() - 7, mainAnchor.y() + 14, SteelBlue.getRGB(), true);
    }

    private static void renderAdjacentItems(GuiGraphics guiGraphics, SurvivalPlayerSnapshot player, Vector2 mainAnchor) {
        ItemStack[] hotbar = player.getHotbar();
        ItemStack leftItem =  hotbar[Math.floorMod(player.selectedHotbarIndex - 1, 9)];
        ItemStack rightItem = hotbar[Math.floorMod(player.selectedHotbarIndex + 1, 9)];

        guiGraphics.renderItem(leftItem, mainAnchor.x() - 6, mainAnchor.y() - 14);
        guiGraphics.renderItem(rightItem,mainAnchor.x() + 23,mainAnchor.y() + 34);

        guiGraphics.renderItemDecorations(DefaultFont, leftItem, mainAnchor.x() - 6, mainAnchor.y() - 14);
        guiGraphics.renderItemDecorations(DefaultFont, rightItem, mainAnchor.x() + 23, mainAnchor.y() + 34);
    }

    private static void renderHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float health, float maxHealth, SurvivalPlayerSnapshot.Effect healthState) {
        Sprite healthBarSprite = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? MM_Sprites.WitheredHealthBar
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? MM_Sprites.PoisonedHealthBar
            : MM_Sprites.MainHealthBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 32,
            mainAnchor.y() + 12,
            healthBarSprite,
            (health / maxHealth),
            SpriteSheetHelper.FillDirection.LeftToRight
        );
    }

    private static void renderGoldenHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float absorption) {
        float maxAbsorption = 20f;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 32,
            mainAnchor.y() + 14,
            MM_Sprites.GoldenHealthBar,
            (absorption / maxAbsorption),
            SpriteSheetHelper.FillDirection.LeftToRight
        );
    }

    private static void renderFoodBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float foodLevel, float maxFoodLevel, SurvivalPlayerSnapshot.Effect hungerState) {
        Sprite hungerBarSprite = (hungerState == SurvivalPlayerSnapshot.Effect.HUNGERED)
            ? MM_Sprites.PoisonedHungerBar
            : MM_Sprites.MainHungerBar;

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 32,
            mainAnchor.y() + 20,
            hungerBarSprite,
            (foodLevel / maxFoodLevel),
            SpriteSheetHelper.FillDirection.LeftToRight
        );
    }

    private static void renderFoodSaturationBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float saturation, float maxFoodLevel) {
        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 32,
            mainAnchor.y() + 24,
            MM_Sprites.SaturationHungerBar,
            (saturation / maxFoodLevel),
            SpriteSheetHelper.FillDirection.LeftToRight
        );
    }

    private static void renderDrowningBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float drowningPercent) {
        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 1,
            mainAnchor.y() + 1,
            MM_Sprites.DrowningHexSprite,
            drowningPercent,
            SpriteSheetHelper.FillDirection.BottomToTop
        );
    }

    private static void renderMountHex(GuiGraphics guiGraphics, Vector2 anchor, LivingVehicleType mountType) {
        // top right
        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() + 19,
            anchor.y() - 18,
            MM_Sprites.SmallHex
        );

        Sprite mountSprite = switch (mountType) {
            case Horse -> MM_Sprites.Horse;
            case Donkey -> MM_Sprites.Donkey;
            case Mule -> MM_Sprites.Donkey;
            case Pig -> MM_Sprites.Pig;
            case Camel -> MM_Sprites.Camel;
            case Skeleton_horse -> MM_Sprites.Skeleton_Horse;
            case Strider -> MM_Sprites.Strider;
            case Unknown -> MM_Sprites.Horse_Silhouette;
        };

        _spriteSheetHelper.blitSprite(
            guiGraphics,
            anchor.x() + 20,
            anchor.y() - 16,
            mountSprite
        );
    }

    private static void renderMountHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, boolean separatedBars, float health, float maxHealth) {
        int xRef = mainAnchor.x() + 41;
        int yRef = mainAnchor.y() - 8;

        if (separatedBars) {
            xRef = mainAnchor.x() + 31;
            yRef = mainAnchor.y() + 26;
        }

        _spriteSheetHelper.blitSprite(
            guiGraphics,
            xRef,
            yRef,
            MM_Sprites.MountHealthBackground
        );

        _spriteSheetHelper.blitMeter(
            guiGraphics,
            mainAnchor.x() + 1,
            mainAnchor.y() + 1,
            MM_Sprites.MountHealthBar,
            (health / maxHealth),
            SpriteSheetHelper.FillDirection.LeftToRight
        );

        if (shouldShowHorseHpNumber()) {
            int alpha = getHorseHpNumbersAlpha();

            int orange = (alpha << threeBytes) | BrightOrange.getRGB();
            guiGraphics.drawString(DefaultFont, String.valueOf(health), xRef + 3, yRef + 6, orange, true);

            int hpWidth = DefaultFont.width(String.valueOf(health)) + 3;
            int gray = (alpha << threeBytes) | MediumGray.getRGB();
            guiGraphics.drawString(DefaultFont, "/" + maxHealth, xRef + hpWidth, yRef + 6, gray, true);
        }
    }

    private static boolean shouldShowHorseHpNumber() {
        Instant now = Instant.now();
        boolean toShow = now.isBefore(PlayerMountData.getWhenPlayerMounted().plus(horseHpNumbersShowTime));
        if (toShow) return true;

        return PlayerMountData.getWhenMountDamagedWhileRiding() != null
            && now.isBefore(PlayerMountData.getWhenMountDamagedWhileRiding().plus(horseHpNumbersShowTime));
    }

    private static int getHorseHpNumbersAlpha() {
        Instant now = Instant.now();
        // Short Time after Player has mounted
        if (now.isBefore(PlayerMountData.getWhenPlayerMounted().plus(horseHpNumbersSolidSeconds))) {
            return 255;
        }

        // Short Time after Mount took damage
        if (
            PlayerMountData.getWhenMountDamagedWhileRiding() != null
            && now.isBefore(PlayerMountData.getWhenMountDamagedWhileRiding().plus(horseHpNumbersSolidSeconds))
        ) {
            return  255;
        }

        // After initial delay, start fading (after mounting)
        if (now.isBefore(PlayerMountData.getWhenPlayerMounted().plus(horseHpNumbersShowTime))) {
            var elapsedSinceMounted = Duration.between(PlayerMountData.getWhenPlayerMounted().plus(horseHpNumbersSolidSeconds), now);
            return calculateAlpha(elapsedSinceMounted, horseHpNumbersFadeSeconds);
        }

        // After initial delay, start fading (after mount took damage)
        if (
            PlayerMountData.getWhenMountDamagedWhileRiding() != null
            && now.isBefore(PlayerMountData.getWhenMountDamagedWhileRiding().plus(horseHpNumbersShowTime))
        ) {
            var elapsedSinceDamaged = Duration.between(PlayerMountData.getWhenMountDamagedWhileRiding().plus(horseHpNumbersSolidSeconds), now);
            return calculateAlpha(elapsedSinceDamaged, horseHpNumbersFadeSeconds);
        }

        return 255;
    }

    private static int calculateAlpha(Duration elapsedTime, Duration totalDuration) {
        double fade = (double)elapsedTime.toNanos() / totalDuration.toNanos() * 255D;
        return Math.max(255 - (int)fade, 25);
    }

    private static void renderItemWithScale(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y, float scaleFactor) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scaleFactor, scaleFactor);
        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.pose().popMatrix();
    }
}
