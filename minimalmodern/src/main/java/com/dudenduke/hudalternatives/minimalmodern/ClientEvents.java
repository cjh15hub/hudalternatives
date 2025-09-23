package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.common.graphics.MC_ResourceLocations;
import com.dudenduke.hudalternatives.common.player.SurvivalPlayerEvent;
import com.dudenduke.hudalternatives.common.player.SurvivalPlayerSnapshot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.time.Duration;
import java.time.Instant;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Constants.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public ClientEvents(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onRegisterGuiOverlay(RegisterGuiLayersEvent event) {
        // Replace and remove vanilla gui layers
        event.replaceLayer(MC_ResourceLocations.HealthGuiLayer, MinimalModernOverlay::emptyRender);
        event.replaceLayer(MC_ResourceLocations.ArmorGuiLayer, MinimalModernOverlay::emptyRender);
        event.replaceLayer(MC_ResourceLocations.FoodGuiLayer, MinimalModernOverlay::emptyRender);
        event.replaceLayer(MC_ResourceLocations.VehicleGuiLayer, MinimalModernOverlay::emptyRender);
        event.replaceLayer(MC_ResourceLocations.AirGuiLayer, MinimalModernOverlay::emptyRender);
        event.replaceLayer(MC_ResourceLocations.XPGuiLayer, MinimalModernOverlay::emptyRender);
        // register our GUI layer
        event.registerAbove(VanillaGuiLayers.HOTBAR, MM_ResourceLocations.MinimalModernGuiLayer, MinimalModernOverlay::render);
    }

    private static final Duration HotbarPreviewDuration = Duration.ofMillis(1500);

    @SubscribeEvent
    static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        // Hotbar Gui layer
        if (event.getName().equals(MC_ResourceLocations.HotbarGuiLayer)) {
            // If recently changed equipment slot
            if (
                SurvivalPlayerEvent.lastItemSlotChangedInstant() == null
                || Instant.now().isAfter(SurvivalPlayerEvent.lastItemSlotChangedInstant().plus(HotbarPreviewDuration))
            ) {
                event.setCanceled(true);
            }
        }
        else if (
            // XP or Horse Jump meter layers
            event.getName().equals(MC_ResourceLocations.XPBarGuiLayer)
        ) {
            final LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            boolean isPlayerMounted = player.getVehicle() instanceof LivingEntity;

            // cancel XP meter layer
            if (!isPlayerMounted) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        MinimalModernHudMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        MinimalModernHudMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void onKeyRegister(RegisterKeyMappingsEvent event){
        MM_KeyBindings.onKeyRegister(event);
    }

    @SubscribeEvent
    static void onKeyInput(InputEvent.Key event) {
        MM_KeyInputHandlers.onKeyInput(event);
    }
}
