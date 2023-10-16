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

    private static final int spriteSheetTextureWidth = 256;
    private static final int spriteSheetTextureHeight = 256;
    private static final float maxHealth = 20f;
    private static final float maxFoodLevel = 20f;
    private static final int mainBackgroundSpriteHeight = 36;
    private static final Dimensions healthSpriteDims = new Dimensions(74, 5);
    private static final Dimensions goldenHealthSpriteDims = new Dimensions(74, 3);
    private static final Dimensions hungerSpriteDims = new Dimensions(60, 3);
    private static final Dimensions hungerSaturationSpriteDims = new Dimensions(60, 1);
    private static final Dimensions drowningSprite = new Dimensions(30, 34);

    public static final IGuiOverlay HUD = new IGuiOverlay() {
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            final LocalPlayer player = gui.getMinecraft().player;
            if (player == null) return;

            final var screenDims = new Dimensions(screenWidth, screenHeight);
            SurvivalPlayerSnapshot playerSnapshot = new SurvivalPlayerSnapshot(player);

            var mainScreenAnchor = getMainScreenAnchorPoint(screenDims);
            renderBackgroundSprite(guiGraphics, mainScreenAnchor);
            renderSmallHexes(guiGraphics, mainScreenAnchor);
            renderActiveItem(gui, guiGraphics, playerSnapshot, mainScreenAnchor);
            renderAdjacentItems(gui, guiGraphics, playerSnapshot, mainScreenAnchor);

            renderHealthBar(guiGraphics, mainScreenAnchor, playerSnapshot.health, playerSnapshot.healthEffect);
            renderGoldenHealthBar(guiGraphics, mainScreenAnchor, playerSnapshot.absorption);
            renderFoodBar(guiGraphics, mainScreenAnchor, playerSnapshot.foodLevel, playerSnapshot.hungerEffect);
            renderFoodSaturationBar(guiGraphics, mainScreenAnchor, playerSnapshot.saturation);

            renderDrowningBar(guiGraphics, mainScreenAnchor, playerSnapshot.drownPercentage);

            renderExperienceLevel(gui, guiGraphics, mainScreenAnchor, playerSnapshot.experienceLevel);
        }

    };

    private static Vector2 getMainScreenAnchorPoint(Dimensions screen) {
        final int spriteBottomYPadding = 20;
        return new Vector2(20, screen.height() - mainBackgroundSpriteHeight - spriteBottomYPadding);
    }

    private static void renderBackgroundSprite(GuiGraphics guiGraphics, Vector2 anchor) {
        final int spriteSourceLocationX = 28;
        final int spriteSourceLocationY = 207;
        final int spriteWidth = 107;

        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x(),
            anchor.y(),
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, mainBackgroundSpriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderSmallHexes(GuiGraphics guiGraphics, Vector2 anchor) {
        final int spriteSourceLocationX = 4;
        final int spriteSourceLocationY = 194;
        final int spriteWidth = 22;
        final int spriteHeight = 24;

        // top left
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() - 9,
            anchor.y() - 18,
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );

        // bottom right
        guiGraphics.blit(
            MINIMAL_MODERN,
            anchor.x() + 20,
            anchor.y() + 30,
            spriteSourceLocationX, spriteSourceLocationY,
            spriteWidth, spriteHeight,
            spriteSheetTextureWidth, spriteSheetTextureHeight
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

    private static void renderHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float health, SurvivalPlayerSnapshot.Effect healthState) {
        final int spriteSourceLocationX = 60;
        // 186, 180, 174
        final int spriteSourceLocationY = (healthState == SurvivalPlayerSnapshot.Effect.WITHERED) ? 174
            : (healthState == SurvivalPlayerSnapshot.Effect.POISONED) ? 180
            : 186;

        final int renderedWidth = ((int)(healthSpriteDims.width() * (health / maxHealth)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 12,
            spriteSourceLocationX, spriteSourceLocationY,
            renderedWidth, healthSpriteDims.height(),
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderGoldenHealthBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float absorption) {
        final int spriteSourceLocationX = 60;
        final int spriteSourceLocationY = 192;

        // float maxAbsorption = 16f;
        float maxAbsorption = 20f;

        final int renderedWidth = ((int)(goldenHealthSpriteDims.width() * (absorption / maxAbsorption)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 14,
            spriteSourceLocationX, spriteSourceLocationY,
            renderedWidth, goldenHealthSpriteDims.height(),
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderFoodBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float foodValue, SurvivalPlayerSnapshot.Effect hungerState) {
        final int spriteSourceLocationX = 60;
        // 186, 180, 174
        final int spriteSourceLocationY = (hungerState == SurvivalPlayerSnapshot.Effect.HUNGERED) ? 200 : 196;

        final int renderedWidth = ((int)(hungerSpriteDims.width() * (foodValue / maxFoodLevel)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 20,
            spriteSourceLocationX, spriteSourceLocationY,
            renderedWidth, hungerSpriteDims.height(),
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderFoodSaturationBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float saturation) {
        final int spriteSourceLocationX = 60;
        final int spriteSourceLocationY = 204;

        final int renderedWidth = ((int)(hungerSaturationSpriteDims.width() * (saturation / maxFoodLevel)));

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 32,
            mainAnchor.y() + 24,
            spriteSourceLocationX, spriteSourceLocationY,
            renderedWidth, hungerSaturationSpriteDims.height(),
            spriteSheetTextureWidth, spriteSheetTextureHeight
        );
    }

    private static void renderDrowningBar(GuiGraphics guiGraphics, Vector2 mainAnchor, float drowningPercent) {
        final int spriteSourceLocationX = 29;
        final int spriteSourceLocationY = 172;

        int removedSpriteHeight = drowningSprite.height() - ((int)(drowningSprite.height() * drowningPercent ));
        int spriteStartY = removedSpriteHeight + spriteSourceLocationY;
        int spriteHeightRemaining = drowningSprite.height() - removedSpriteHeight;

        guiGraphics.blit(
            MINIMAL_MODERN,
            mainAnchor.x() + 1,
            mainAnchor.y() + 1 + removedSpriteHeight,
            spriteSourceLocationX, spriteStartY,
            drowningSprite.width(), spriteHeightRemaining,
            spriteSheetTextureWidth, spriteSheetTextureHeight
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
