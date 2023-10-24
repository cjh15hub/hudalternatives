package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.Constants;
import com.dudenduke.hudalternatives.utils.Dimensions;
import com.dudenduke.hudalternatives.utils.SurvivalPlayerSnapshot;
import com.dudenduke.hudalternatives.utils.Vector2;
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

public class MinimalModernOverlay {
    private static final ResourceLocation MINIMAL_MODERN = new ResourceLocation(Constants.MODID, "textures/minimal_modern.png");

    public static final IGuiOverlay HUD = new IGuiOverlay() {
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            final LocalPlayer player = gui.getMinecraft().player;
            if (player == null) return;

            final var screenDims = new Dimensions(screenWidth, screenHeight);
            SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);

            // Standard reference point for all gui elements
            Vector2 mainScreenAnchor = getMainScreenAnchorPoint(screenDims, MM_Configuration.MainGuiDrawCorner());
            var valueBarAnchorPoint = mainScreenAnchor;  // if separated, adjusted anchor point for value bars


            // Main Elements and Value Bars Separated
            if (MM_Configuration.SeparateValueBars() && MM_Configuration.ValueBarsDrawCorner() != MM_Configuration.MainGuiDrawCorner()) {
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
            renderSmallHexes(guiGraphics, mainScreenAnchor);
            renderActiveItem(gui, guiGraphics, playerSnapshot, mainScreenAnchor);
            renderAdjacentItems(gui, guiGraphics, playerSnapshot, mainScreenAnchor);

            // Value Bars
            renderHealthBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.health, playerSnapshot.maxHealth, playerSnapshot.healthEffect);
            renderGoldenHealthBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.absorption);
            renderFoodBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.foodLevel, playerSnapshot.maxFoodLevel, playerSnapshot.hungerEffect);
            renderFoodSaturationBar(guiGraphics, valueBarAnchorPoint, playerSnapshot.saturation, playerSnapshot.maxFoodLevel);

            // Misc Survival
            renderDrowningBar(guiGraphics, mainScreenAnchor, playerSnapshot.drownPercentage);
            renderExperienceLevel(gui, guiGraphics, mainScreenAnchor, playerSnapshot.experienceLevel);
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

    private static void renderSmallHexes(GuiGraphics guiGraphics, Vector2 anchor) {
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
        var color = TextColor.parseColor("#00e968").getValue();
        guiGraphics.drawString(gui.getFont(), String.valueOf(experienceLevel), mainAnchor.x() + 26, mainAnchor.y() - 3, color, true);
    }

    private static void renderActiveItem(ForgeGui gui, GuiGraphics guiGraphics, SurvivalPlayerSnapshot player, Vector2 mainAnchor) {
        final float scaleFactor = 1.5f;
        _renderItemWithScale(gui, guiGraphics, player.localPlayer, player.mainHandItem, mainAnchor.x() + 8, mainAnchor.y() + 11, scaleFactor);
        guiGraphics.renderItemDecorations(gui.getFont(), player.mainHandItem, mainAnchor.x() + 8, mainAnchor.y() + 13);

        var color = TextColor.parseColor("#008db8").getValue();
        guiGraphics.drawString(gui.getFont(), String.valueOf(player.selectedHotbarIndex + 1), mainAnchor.x() - 7, mainAnchor.y() + 14, color, true);
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

            // pose.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
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
