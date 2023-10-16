package com.dudenduke.hudalternatives;

import com.dudenduke.hudalternatives.deviljars.DevilJarsHudOverlay;
import com.dudenduke.hudalternatives.minimalmodern.MM_KeyBindings;
import com.dudenduke.hudalternatives.minimalmodern.MM_KeyInputHandlers;
import com.dudenduke.hudalternatives.minimalmodern.MinimalModernOverlay;
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

            switch (Constants.ModBuild) {
                case MINIMAL_MODERN -> event.registerAboveAll("minimal_modern_hud", MinimalModernOverlay.HUD);
                case DEVIL_JARS -> event.registerAboveAll("devil_jars_hud", DevilJarsHudOverlay.DEVIL_JARS_HUD);
                default -> throw new IllegalArgumentException("Unrecognized build");
            }
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            if (Constants.ModBuild == ModBuildCase.MINIMAL_MODERN) MM_KeyBindings.onKeyRegister(event);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
    public static class ClientModForgeBusEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (Constants.ModBuild == ModBuildCase.MINIMAL_MODERN) MM_KeyInputHandlers.onKeyInput(event);
        }
    }
}
