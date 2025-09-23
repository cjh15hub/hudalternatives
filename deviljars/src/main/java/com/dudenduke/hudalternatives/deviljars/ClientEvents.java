package com.dudenduke.hudalternatives.deviljars;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@net.neoforged.fml.common.Mod(value = Constants.MODID, dist = net.neoforged.api.distmarker.Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Constants.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT)
public class ClientEvents {
    public ClientEvents(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @net.neoforged.bus.api.SubscribeEvent
    static void onRegisterGuiOverlay(RegisterGuiLayersEvent event) {
        event.replaceLayer(ResourceLocation.fromNamespaceAndPath("minecraft", "player_health"), DevilJarsHudOverlay::emptyRender);
        event.replaceLayer(ResourceLocation.fromNamespaceAndPath("minecraft", "food_level"), DevilJarsHudOverlay::emptyRender);

        event.registerBelow(VanillaGuiLayers.HOTBAR, DevilJarsHudOverlay.DEVIL_JARS, DevilJarsHudOverlay::render);
    }

    @net.neoforged.bus.api.SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        DevilJarsHudMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        DevilJarsHudMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
