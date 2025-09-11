package com.dudenduke.hudalternatives.common;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> MELEE_WEAPONS = createTag("c", "tools/melee_weapon");
        public static final TagKey<Item> RANGED_WEAPONS = createTag("c", "tools/ranged_weapon");

        public static TagKey<Item> createTag(String namespace, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
