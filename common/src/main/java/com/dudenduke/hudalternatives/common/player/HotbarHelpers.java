package com.dudenduke.hudalternatives.common.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class HotbarHelpers {
    private HotbarHelpers() {}

    public static ItemStack[] getHotbar(LocalPlayer player) {
        ItemStack[] hotbar = new ItemStack[9];
        for (int i = 0; i < hotbar.length; i++) {
            hotbar[i] = player.getInventory().getItem(i);
        }
        return  hotbar;
    }

    public static int findCorrectToolInHotbar(LocalPlayer player) {
        if (player == null) return -1;

        HitResult blockHit = player.pick(5.0d, 0.0f, false);
        if (blockHit.getType() != HitResult.Type.BLOCK) {
            return -1;
        }

        var blockPos = ((BlockHitResult)blockHit).getBlockPos();
        var blockState = player.level().getBlockState(blockPos);

        var blockStateTags = blockState.getTags().toList();

        var itemTagKey = (blockStateTags.contains(BlockTags.MINEABLE_WITH_PICKAXE)) ? ItemTags.PICKAXES
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_AXE)) ? ItemTags.AXES
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_SHOVEL)) ? ItemTags.SHOVELS
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_HOE)) ? ItemTags.HOES
            : null;

        if (itemTagKey == null) {
            return -1;
        }

        ItemStack[] hotbar = getHotbar(player);
        int toolIndex = -1;
        for (int i = 0; i < hotbar.length; i++) {
            if (hotbar[i].getTags().toList().contains(itemTagKey)) {
                toolIndex = i;
                break;
            }
        }
        return toolIndex;
    }

    public static int findNextMeleeWeaponInHotbar(LocalPlayer player) {
        return findNextWeaponOfTypeInHotbar(player, WeaponType.MELEE);
    }

    public static int findNextRangedWeaponInHotbar(LocalPlayer player) {
        return findNextWeaponOfTypeInHotbar(player, WeaponType.RANGED);
    }

    private static int findNextWeaponOfTypeInHotbar(LocalPlayer player, WeaponType weaponType) {
        final ItemStack[] hotbar = getHotbar(player);
        final int selectedHotbarIndex = player.getInventory().selected;
        int nextWeaponIndex = -1;

        int startingIndex = 0;
        if (weaponType == WeaponType.MELEE) {
            startingIndex = WeaponUtils.isMeleeWeapon(player.getMainHandItem()) ? selectedHotbarIndex + 1 : 0;
        }
        else if (weaponType == WeaponType.RANGED) {
            startingIndex = WeaponUtils.isRangedWeapon(player.getMainHandItem()) ? selectedHotbarIndex + 1 : 0;
        }
        else {
            // TODO: Not implemented
            return -1;
        }

        for (int i = 0; i < hotbar.length; i++) {
            int nextIndex = Math.floorMod(startingIndex + i, 9);

            if (weaponType == WeaponType.MELEE && WeaponUtils.isMeleeWeapon(hotbar[nextIndex])) {
                nextWeaponIndex = nextIndex;
                break;
            }
            else if (weaponType == WeaponType.RANGED && WeaponUtils.isRangedWeapon(hotbar[nextIndex])) {
                nextWeaponIndex = nextIndex;
                break;
            }
        }

        return nextWeaponIndex;
    }

}
