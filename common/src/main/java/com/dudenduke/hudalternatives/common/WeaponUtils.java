package com.dudenduke.hudalternatives.common;

import net.minecraft.world.item.*;

public class WeaponUtils {

   public static boolean isMeleeWeapon(ItemStack itemStack) {
       return itemStack.is(ModTags.Items.MELEE_WEAPONS);
   }

    public static boolean isRangedWeapon(ItemStack itemStack) {
       return itemStack.is(ModTags.Items.RANGED_WEAPONS);
    }
}
