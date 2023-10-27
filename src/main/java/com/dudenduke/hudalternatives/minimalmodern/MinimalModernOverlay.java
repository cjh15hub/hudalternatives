package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.Constants;
import com.dudenduke.hudalternatives.utils.*;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Matrix4f;

import java.time.Duration;
import java.time.Instant;

public class MinimalModernOverlay {
    private static final ResourceLocation MINIMAL_MODERN = new ResourceLocation(Constants.MODID, "textures/minimal_modern.png");

    private static final Duration horseHpNumbersSolidSeconds = Duration.ofMillis(1500);
    private static final Duration horseHpNumbersFadeSeconds = Duration.ofMillis(1500);
    private static final Duration horseHpNumbersShowTime = horseHpNumbersSolidSeconds.plus(horseHpNumbersFadeSeconds);

    public static final IGuiOverlay HUD = new IGuiOverlay() {
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            final LocalPlayer player = gui.getMinecraft().player;
            if (player == null) return;

            final var screenDims = new Dimensions(screenWidth, screenHeight);
            SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);
            PlayerMountData.updateMountData(player);

            // Standard reference point for all gui elements
            Vector2 mainScreenAnchor = getMainScreenAnchorPoint(screenDims, MM_Configuration.MainGuiDrawCorner());
            var valueBarAnchorPoint = mainScreenAnchor;  // if separated, adjusted anchor point for value bars


            // Main Elements and Value Bars Separated
            var separatedBars = (MM_Configuration.SeparateValueBars() && MM_Configuration.ValueBarsDrawCorner() != MM_Configuration.MainGuiDrawCorner());
            if (separatedBars) {
                // Main GUI rendered left side
                if (MM_Configuration.MainGuiDrawCorner() == 1 || MM_Configuration.MainGuiDrawCorner() == 3) {
                    var adjustedX = mainScreenAnchor.x() + MM_Sprites.ValueBarsBackground.width() - (MM_Sprites.SmallHex.width() / 2) - 1;
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
            renderActiveItem(gui, guiGraphics, playerSnapshot, mainScreenAnchor);
            renderAdjacentItems(gui, guiGraphics, playerSnapshot, mainScreenAnchor);

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

                var mountHealthBarAnchorPoint = (!separatedBars)
                    ? mainScreenAnchor
                    : getSeparatedValueBarsAnchorPoint(screenDims, MM_Configuration.ValueBarsDrawCorner());
                renderMountHealthBar(gui, guiGraphics, mountHealthBarAnchorPoint, separatedBars, PlayerMountData.getMountHealth(), PlayerMountData.getMountMaxHealth());
            }
            else {
                renderExperienceLevel(gui, guiGraphics, mainScreenAnchor, playerSnapshot.experienceLevel);
            }
        }

    };

    private static Vector2 getMainScreenAnchorPoint(Dimensions screen, int drawCorner) {
        var leftX = 10 +MM_Configuration.MainGuiHorizontalPadding();
        var rightX = screen.width() - MM_Sprites.LargeHex.width() - MM_Sprites.MainHealthBar.width() -2 -MM_Configuration.MainGuiHorizontalPadding();
        var topY = 19 +MM_Configuration.MainGuiVerticalPadding();
        var bottomY = screen.height() - MM_Sprites.LargeHex.height() - 19 -MM_Configuration.MainGuiVerticalPadding();

        return switch(drawCorner) {
            case 0 -> new Vector2(leftX, topY);     // top left
            case 1 -> new Vector2(rightX, topY);    // top right
            case 2 -> new Vector2(leftX, bottomY);  // bottom left
            case 3 -> new Vector2(rightX, bottomY); // bottom right
            default -> new Vector2(leftX, bottomY); // bottom left (default)
        };
    }

    private static Vector2 getSeparatedValueBarsAnchorPoint(Dimensions screen, int drawCorner) {
        var leftX = 4 - MM_Sprites.LargeHex.width() +MM_Configuration.ValueBarsHorizontalPadding();
        var rightX = screen.width() - MM_Sprites.LargeHex.width() - MM_Sprites.MainHealthBar.width() -2 -MM_Configuration.ValueBarsHorizontalPadding();
        var topY = 19 - MM_Sprites.SmallHex.height() +MM_Configuration.ValueBarsVerticalPadding();
        var bottomY = screen.height() - MM_Sprites.LargeHex.height() - 19 + MM_Sprites.SmallHex.height() -MM_Configuration.ValueBarsVerticalPadding();

        return switch(drawCorner) {
            case 0 -> new Vector2(leftX, topY);     // top left
            case 1 -> new Vector2(rightX, topY);    // top right
            case 2 -> new Vector2(leftX, bottomY);  // bottom left
            case 3 -> new Vector2(rightX, bottomY); // bottom right
            default -> new Vector2(leftX, bottomY); // bottom left (default)
        };
    }

    private static void renderMainHex(GuiGraphics guiGraphics, Vector2 anchor) {
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x(),
            anchor.y(),
            MM_Sprites.LargeHex.x(), MM_Sprites.LargeHex.y(),
            MM_Sprites.LargeHex.width(), MM_Sprites.LargeHex.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderValueBarsBackground(GuiGraphics guiGraphics, Vector2 anchor) {
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() + 31,
            anchor.y() + 11,
            MM_Sprites.ValueBarsBackground.x(), MM_Sprites.ValueBarsBackground.y(),
            MM_Sprites.ValueBarsBackground.width(), MM_Sprites.ValueBarsBackground.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderHotbarHexes(GuiGraphics guiGraphics, Vector2 anchor) {
        // top left
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() - 9,
            anchor.y() - 18,
            MM_Sprites.SmallHex.x(), MM_Sprites.SmallHex.y(),
            MM_Sprites.SmallHex.width(), MM_Sprites.SmallHex.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );

        // bottom right
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() + 20,
            anchor.y() + 30,
            MM_Sprites.SmallHex.x(), MM_Sprites.SmallHex.y(),
            MM_Sprites.SmallHex.width(), MM_Sprites.SmallHex.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderExperienceLevel(ForgeGui gui, GuiGraphics guiGraphics, Vector2 mainAnchor, int experienceLevel) {
        var green = 0x00e968;
        guiGraphics.drawString(gui.getFont(), String.valueOf(experienceLevel), mainAnchor.x() + 26, mainAnchor.y() - 3, green, true);
    }

    private static void renderActiveItem(ForgeGui gui, GuiGraphics guiGraphics, SurvivalPlayerSnapshot player, Vector2 mainAnchor) {
        final float scaleFactor = 1.5f;
        _renderItemWithScale(gui, guiGraphics, player.localPlayer, player.mainHandItem, mainAnchor.x() + 8, mainAnchor.y() + 11, scaleFactor);
        guiGraphics.renderItemDecorations(gui.getFont(), player.mainHandItem, mainAnchor.x() + 8, mainAnchor.y() + 13);

        var blue = 0x008db8;
        guiGraphics.drawString(gui.getFont(), String.valueOf(player.selectedHotbarIndex + 1), mainAnchor.x() - 7, mainAnchor.y() + 14, blue, true);
    }

    private static void renderAdjacentItems(ForgeGui gui, GuiGraphics guiGraphics, SurvivalPlayerSnapshot player, Vector2 mainAnchor) {
        var hotbar = player.getHotbar();
        var leftItem =  hotbar[Math.floorMod(player.selectedHotbarIndex - 1, 9)];
        var rightItem = hotbar[Math.floorMod(player.selectedHotbarIndex + 1, 9)];

        guiGraphics.renderItem(leftItem, mainAnchor.x() - 6, mainAnchor.y() - 14);
        guiGraphics.renderItem(rightItem,mainAnchor.x() + 23,mainAnchor.y() + 34);

        guiGraphics.renderItemDecorations(gui.getFont(), leftItem, mainAnchor.x() - 6, mainAnchor.y() - 14);
        guiGraphics.renderItemDecorations(gui.getFont(), rightItem, mainAnchor.x() + 23, mainAnchor.y() + 34);
    }

    private static void renderHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float health, float maxHealth, SurvivalPlayerSnapshot.Effect healthState) {
        var healthBarSprite = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? MM_Sprites.WitheredHealthBar
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? MM_Sprites.PoisonedHealthBar
            : MM_Sprites.MainHealthBar;

        final int renderedWidth = ((int)(healthBarSprite.width() * (health / maxHealth)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 12,
            healthBarSprite.x(), healthBarSprite.y(),
            renderedWidth, healthBarSprite.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderGoldenHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float absorption) {
        float maxAbsorption = 20f;
        final int renderedWidth = ((int)(MM_Sprites.GoldenHealthBar.width() * (absorption / maxAbsorption)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 14,
            MM_Sprites.GoldenHealthBar.x(), MM_Sprites.GoldenHealthBar.y(),
            renderedWidth, MM_Sprites.GoldenHealthBar.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderFoodBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float foodLevel, float maxFoodLevel, SurvivalPlayerSnapshot.Effect hungerState) {
        var hungerBarSprite = (hungerState == SurvivalPlayerSnapshot.Effect.HUNGERED)
            ? MM_Sprites.PoisonedHungerBar
            : MM_Sprites.MainHungerBar;

        final int renderedWidth = ((int)(hungerBarSprite.width() * (foodLevel / maxFoodLevel)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 20,
            hungerBarSprite.x(), hungerBarSprite.y(),
            renderedWidth, hungerBarSprite.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderFoodSaturationBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float saturation, float maxFoodLevel) {
        final int renderedWidth = ((int)(MM_Sprites.SaturationHungerBar.width() * (saturation / maxFoodLevel)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 24,
            MM_Sprites.SaturationHungerBar.x(), MM_Sprites.SaturationHungerBar.y(),
            renderedWidth, MM_Sprites.SaturationHungerBar.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderDrowningBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float drowningPercent) {
        int removedSpriteHeight = MM_Sprites.DrowingHexSprite.height() - ((int)(MM_Sprites.DrowingHexSprite.height() * drowningPercent ));
        int spriteStartY = removedSpriteHeight + MM_Sprites.DrowingHexSprite.y();
        int spriteHeightRemaining = MM_Sprites.DrowingHexSprite.height() - removedSpriteHeight;

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 1,
            mainAnchor.y() + 1 + removedSpriteHeight,
            MM_Sprites.DrowingHexSprite.x(), spriteStartY,
            MM_Sprites.DrowingHexSprite.width(), spriteHeightRemaining,
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }


    private static void renderMountHex(GuiGraphics guiGraphics, Vector2 anchor, LivingVehicleType mountType) {
        // top right
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() + 19,
            anchor.y() - 18,
            MM_Sprites.SmallHex.x(), MM_Sprites.SmallHex.y(),
            MM_Sprites.SmallHex.width(), MM_Sprites.SmallHex.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );

        var mountSprite = switch (mountType) {
            case Horse -> MM_Sprites.Horse;
            case Donkey -> MM_Sprites.Donkey;
            case Mule -> MM_Sprites.Donkey;
            case Pig -> MM_Sprites.Pig;
            case Camel -> MM_Sprites.Camel;
            case Skeleton_horse -> MM_Sprites.Skeleton_Horse;
            case Strider -> MM_Sprites.Strider;
            case Unknown -> MM_Sprites.Horse_Silhouette;
            default -> MM_Sprites.Horse_Silhouette;
        };

        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() + 20,
            anchor.y() - 16,
            mountSprite.x(), mountSprite.y(),
            mountSprite.width(), mountSprite.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );
    }

    private static void renderMountHealthBar(ForgeGui gui, GuiGraphics guiGraphics, Vector2 mainAnchor, boolean separatedBars, float health, float maxHealth) {
        var xRef = mainAnchor.x() + 41;
        var yRef = mainAnchor.y() - 8;

        if (separatedBars) {
            xRef = mainAnchor.x() + 31;
            yRef = mainAnchor.y() + 26;
        }

        guiGraphics.blit(
            MINIMAL_MODERN,
            xRef,
            yRef,
            MM_Sprites.MountHealthBackground.x(), MM_Sprites.MountHealthBackground.y(),
            MM_Sprites.MountHealthBackground.width(), MM_Sprites.MountHealthBackground.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );

        final int renderedWidth = ((int)(MM_Sprites.MountHealthBar.width() * (health / maxHealth)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            xRef + 1,
            yRef + 1,
            MM_Sprites.MountHealthBar.x(), MM_Sprites.MountHealthBar.y(),
            renderedWidth, MM_Sprites.MountHealthBar.height(),
            MM_Sprites.FullSheet.width(), MM_Sprites.FullSheet.height()
        );

        if (shouldShowHorseHpNumber()) {
            var alpha = getHorseHpNumbersAlpha();
            final int threeBytes = 24;

            var orange = (alpha << threeBytes) | 0xD65410;
            guiGraphics.drawString(gui.getFont(), String.valueOf(health), xRef + 3, yRef + 6, orange, true);

            var hpWidth = gui.getFont().width(String.valueOf(health)) + 3;
            var gray = (alpha << threeBytes) | 0x5C5C5C;
            guiGraphics.drawString(gui.getFont(), "/" + maxHealth, xRef + hpWidth, yRef + 6, gray, true);
        }
    }

    private static boolean shouldShowHorseHpNumber() {
        var now = Instant.now();
        var toShow = now.isBefore(PlayerMountData.getWhenPlayerMounted().plus(horseHpNumbersShowTime));
        if (toShow) return true;

        return PlayerMountData.getWhenMountDamagedWhileRiding() != null
            && now.isBefore(PlayerMountData.getWhenMountDamagedWhileRiding().plus(horseHpNumbersShowTime));
    }

    private static int getHorseHpNumbersAlpha() {
        var now = Instant.now();
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
        var fade = (double)elapsedTime.toNanos() / totalDuration.toNanos() * 255D;
        return Math.max(255 - (int)fade, 25);
    }


    private static void _renderItemWithScale(ForgeGui gui, GuiGraphics guiGraphics, LocalPlayer player, ItemStack itemStack, int x, int y, float scaleFactor) {
        _renderItemWithScale(gui.getMinecraft(), guiGraphics.bufferSource(), guiGraphics.pose(), player, gui.getMinecraft().level, itemStack, x, y, scaleFactor);
    }

    private static void _renderItemWithScale(
        Minecraft minecraft, MultiBufferSource.BufferSource bufferSource, PoseStack pose, LivingEntity pEntity, Level pLevel,
        ItemStack pStack, int pX, int pY, float scaleFactor
    ) {
        if (!pStack.isEmpty()) {
            BakedModel bakedmodel = minecraft.getItemRenderer().getModel(pStack, pLevel, pEntity, 0);
            pose.pushPose();
            pose.translate((float)(pX + 8), (float)(pY + 8), (float)(150));

            pose.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));

            final float adjustedScale = 16.0F * scaleFactor;

            //pose.scale(16.0F, 16.0F, 16.0F);
            pose.scale(adjustedScale, adjustedScale, adjustedScale);

            boolean flag = !bakedmodel.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }

            minecraft.getItemRenderer().render(pStack, ItemDisplayContext.GUI, false, pose, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

            //this.flush();
            RenderSystem.disableDepthTest();
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();


            if (flag) {
                Lighting.setupFor3DItems();
            }

            pose.popPose();
        }
    }
}
