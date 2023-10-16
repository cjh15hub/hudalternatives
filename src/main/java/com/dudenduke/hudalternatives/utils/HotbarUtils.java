package com.dudenduke.hudalternatives.utils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;

public class HotbarUtils {
    public static int findBestMeleeWeaponInHotbar(LocalPlayer player, WeaponType weaponType) {
        if (player == null) return -1;

        float bestSwordAttackDamage = 0;
        int bestSwordIndex = -1;

        float bestAxeAttackDamage = 0;
        int bestAxeIndex = -1;

        var inventory = player.getInventory();
        for (int i = 0; i < 9; i++) {
            var slot = inventory.getItem(i);
            if (slot.is(ItemTags.SWORDS) || slot.is(ItemTags.AXES)) {
                if (slot.getItem() instanceof SwordItem swordItem) {
                    var swordItemAttackDamage = swordItem.getDamage();
                    if (bestSwordAttackDamage == 0 || swordItemAttackDamage > bestSwordAttackDamage) {
                        bestSwordAttackDamage = swordItemAttackDamage;
                        bestSwordIndex = i;
                    }
                }
                else if (slot.getItem() instanceof AxeItem axeItem) {
                    var axeItemAttackDamage = axeItem.getAttackDamage();
                    if (bestAxeAttackDamage == 0 || axeItemAttackDamage > bestAxeAttackDamage) {
                        bestAxeAttackDamage = axeItemAttackDamage;
                        bestAxeIndex = i;
                    }
                }
            }
        }

        if (weaponType == WeaponType.ANY) {
            var firstWeaponIndex = Math.min(bestSwordIndex, bestAxeIndex);
            if (firstWeaponIndex != -1) {
                return firstWeaponIndex;
            }
            else if (bestSwordIndex == -1 && bestAxeIndex == -1) {
                return -1;
            }
            else if (bestAxeIndex == -1) {
                return bestSwordIndex;
            }
            else {
                return bestAxeIndex;
            }
        }
        else if (weaponType == WeaponType.SWORD && bestSwordAttackDamage != 0) {
            return bestSwordIndex;
        }
        else if (weaponType == WeaponType.AXE && bestAxeAttackDamage != 0) {
            return bestAxeIndex;
        }

        return -1;
    }

}
