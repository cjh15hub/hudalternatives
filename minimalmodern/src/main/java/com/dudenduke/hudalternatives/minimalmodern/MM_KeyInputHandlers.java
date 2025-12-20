package com.dudenduke.hudalternatives.minimalmodern;

public class MM_KeyInputHandlers {

    public static void clientTick() {
        if (MM_KeyBindings.TOGGLE_HUD.consumeClick()) {
            MinimalModernOverlay.toggle();
        }
    }
}
