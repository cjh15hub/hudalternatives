package com.dudenduke.hudalternatives.minimalmodern;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientEvents {

    // === MOD EVENT BUS (INIT TIME) ===
    @Mod.EventBusSubscriber(
            modid = Constants.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ModBus {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll(
                    new ResourceLocation(Constants.MODID, "minimal_modern_hud"),
                    MinimalModernOverlay.HUD
            );
        }

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            MM_KeyBindings.onKeyRegister(event);
        }
    }

    // === FORGE EVENT BUS (RUNTIME) ===
    @Mod.EventBusSubscriber(
            modid = Constants.MODID,
            value = Dist.CLIENT
    )
    public static class ForgeBus {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                MM_KeyInputHandlers.clientTick();
            }
        }
    }
}
