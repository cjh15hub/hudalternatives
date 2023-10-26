package com.dudenduke.hudalternatives.minimalmodern;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {

            event.registerAboveAll("minimal_modern_hud", MinimalModernOverlay.HUD);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            MM_KeyBindings.onKeyRegister(event);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
    public static class ClientModForgeBusEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            MM_KeyInputHandlers.onKeyInput(event);
        }
    }
}
