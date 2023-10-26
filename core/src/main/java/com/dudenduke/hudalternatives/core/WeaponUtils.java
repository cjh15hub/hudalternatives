package com.dudenduke.hudalternatives.core;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class WeaponUtils {

    public static boolean isMeleeWeapon(ItemStack itemStack) {
        return itemStack.is(ModTags.ModItemTags.MELEE_WEAPONS)
            || (itemStack.getItem() instanceof SwordItem);
    }

    public static boolean isRangedWeapon(ItemStack itemStack) {
        return  itemStack.is(ModTags.ModItemTags.RANGED_WEAPONS)
            || (itemStack.getItem() instanceof BowItem);
    }
}
