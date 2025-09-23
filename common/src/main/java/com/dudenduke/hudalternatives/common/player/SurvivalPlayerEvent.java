package com.dudenduke.hudalternatives.common.player;

import java.time.Instant;

public class SurvivalPlayerEvent {
    private  SurvivalPlayerEvent() {}

    private static int _lastSelectedHotbarIndex = -1;
    private static Instant _LastItemSlotChangedInstant = null;

    public static int lastSelectedHotbarIndex() {
        return _lastSelectedHotbarIndex;
    }

    public static void setLastSelectedHotbarIndex(int index) {
        _lastSelectedHotbarIndex = index;
    }

    public static Instant lastItemSlotChangedInstant() {
        return _LastItemSlotChangedInstant;
    }

    public static void setLastItemSlotChangedInstant() {
        _LastItemSlotChangedInstant = Instant.now();
    }

    public static void setLastItemSlotChangedInstant(Instant instant) {
        _LastItemSlotChangedInstant = instant;
    }
}
