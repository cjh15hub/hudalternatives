package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.utils.SurvivalPlayerSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.event.InputEvent;

public class MM_KeyInputHandlers {

    public static void onKeyInput(InputEvent.Key event) {
        var player = Minecraft.getInstance().player;

        // Find Melee Weapon
        if (MM_KeyBindings.FIND_MELEE_WEAPON_INPUT.consumeClick()) {
            FindMeleeWeapon(player);
        }

        // Find Ranged Weapon
        if (MM_KeyBindings.FIND_RANGED_WEAPON_INPUT.consumeClick()) {
            FindRangerWeapon(player);
        }

        // Find Correct Tool
        if (MM_KeyBindings.FIND_TOOL_INPUT.consumeClick()) {
            FindCorrectTool(player);
        }
    }

    public static void FindMeleeWeapon(LocalPlayer player) {
        if (player == null) return;

        var playerSnapshot = new SurvivalPlayerSnapshot(player);

        var nextWeaponIndex = playerSnapshot.findNextMeleeWeaponInHotbar();
        if (nextWeaponIndex != -1 && nextWeaponIndex != playerSnapshot.selectedHotbarIndex) {
            player.getInventory().selected = nextWeaponIndex;
            player.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE, 0.6f, 1.5f);
        }
        else if (!SurvivalPlayerSnapshot.isMeleeWeapon(playerSnapshot.mainHandItem)) {
            player.playSound(SoundEvents.ITEM_FRAME_PLACE, 0.6f, 0.5f);
        }
    }

    public static void FindRangerWeapon(LocalPlayer player) {
        if (player == null) return;

        var playerSnapshot = new SurvivalPlayerSnapshot(player);

        var nextWeaponIndex = playerSnapshot.findNextRangedWeaponInHotbar();
        if (nextWeaponIndex != -1 && nextWeaponIndex != playerSnapshot.selectedHotbarIndex) {
            player.getInventory().selected = nextWeaponIndex;
            player.playSound(SoundEvents.WOOL_BREAK, 0.6f, 1.5f);
        }
        else if (!SurvivalPlayerSnapshot.isRangedWeapon(playerSnapshot.mainHandItem)) {
            player.playSound(SoundEvents.ITEM_FRAME_PLACE, 0.6f, 0.5f);
        }
    }

    public static void FindCorrectTool(LocalPlayer player) {
        if (player == null) return;

        var playerSnapshot = new SurvivalPlayerSnapshot(player);
        var correctToolIndex = playerSnapshot.findCorrectToolInHotbar();
        if (correctToolIndex != -1 && correctToolIndex != playerSnapshot.selectedHotbarIndex) {
            player.getInventory().selected = correctToolIndex;
            player.playSound(SoundEvents.WOOL_BREAK, 0.6f, 1.5f);
        }
        else if (correctToolIndex == -1) {
            player.playSound(SoundEvents.WOODEN_TRAPDOOR_CLOSE, 0.6f, 0.5f);
        }
    }

}
