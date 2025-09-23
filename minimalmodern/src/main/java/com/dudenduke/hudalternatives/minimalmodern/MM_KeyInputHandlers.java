package com.dudenduke.hudalternatives.minimalmodern;

import com.dudenduke.hudalternatives.common.player.HotbarHelpers;
import com.dudenduke.hudalternatives.common.player.WeaponUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.event.InputEvent;

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

        int nextWeaponIndex = HotbarHelpers.findNextMeleeWeaponInHotbar(player);
        if (nextWeaponIndex != -1 && nextWeaponIndex != player.getInventory().selected) {
            player.getInventory().selected = (nextWeaponIndex);
            player.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE, 0.6f, 1.5f);
        }
        else if (!WeaponUtils.isMeleeWeapon(player.getMainHandItem())) {
            player.playSound(SoundEvents.ITEM_FRAME_PLACE, 0.6f, 0.5f);
        }
    }

    public static void FindRangerWeapon(LocalPlayer player) {
        if (player == null) return;

        int nextWeaponIndex = HotbarHelpers.findNextRangedWeaponInHotbar(player);
        if (nextWeaponIndex != -1 && nextWeaponIndex != player.getInventory().selected) {
            player.getInventory().selected = (nextWeaponIndex);
            player.playSound(SoundEvents.WOOL_BREAK, 0.6f, 1.5f);
        }
        else if (!WeaponUtils.isRangedWeapon(player.getMainHandItem())) {
            player.playSound(SoundEvents.ITEM_FRAME_PLACE, 0.6f, 0.5f);
        }
    }

    public static void FindCorrectTool(LocalPlayer player) {
        if (player == null) return;

        int correctToolIndex = HotbarHelpers.findCorrectToolInHotbar(player);
        if (correctToolIndex != -1 && correctToolIndex != player.getInventory().selected) {
            player.getInventory().selected = (correctToolIndex);
            player.playSound(SoundEvents.WOOL_BREAK, 0.6f, 1.5f);
        }
        else if (correctToolIndex == -1) {
            player.playSound(SoundEvents.WOODEN_TRAPDOOR_CLOSE, 0.6f, 0.5f);
        }
    }
}
