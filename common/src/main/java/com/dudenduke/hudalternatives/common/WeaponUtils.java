package com.dudenduke.hudalternatives.common;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;

public class WeaponUtils {

    public static boolean isMeleeWeapon(ItemStack itemStack) {
        return itemStack.is(ModTags.ModItemTags.MELEE_WEAPONS)
            || (itemStack.getItem() instanceof SwordItem)
            || (itemStack.getItem() instanceof AxeItem);
    }

    public static boolean isRangedWeapon(ItemStack itemStack) {
        return  itemStack.is(ModTags.ModItemTags.RANGED_WEAPONS)
            || (itemStack.getItem() instanceof BowItem)
            || (itemStack.getItem() instanceof CrossbowItem)
            || (itemStack.getItem() instanceof TridentItem);
    }
}
