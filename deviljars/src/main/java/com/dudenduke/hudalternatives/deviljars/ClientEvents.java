package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.common.graphics.MC_ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

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
        event.registerBelow(VanillaGuiLayers.HOTBAR, DevilJarsHudOverlay.DEVIL_JARS, DevilJarsHudOverlay::render);
    }

    @SubscribeEvent
    static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        final ResourceLocation eventName = event.getName();
        if (
            eventName.equals(MC_ResourceLocations.HealthGuiLayer)
            || eventName.equals(MC_ResourceLocations.FoodGuiLayer)
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        DevilJarsHudMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        DevilJarsHudMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
