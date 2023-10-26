package com.dudenduke.hudalternatives.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class ModItemTags {

        public static  final TagKey<Item> WEAPONS = forgeTag("weapons");

        public static  final TagKey<Item> MELEE_WEAPONS = forgeTag("melee_weapons");

        public static  final TagKey<Item> RANGED_WEAPONS = forgeTag("ranged_weapons");

        private static TagKey<Item> tag(String modId, String name) {
            return ItemTags.create(new ResourceLocation(modId, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }
}
