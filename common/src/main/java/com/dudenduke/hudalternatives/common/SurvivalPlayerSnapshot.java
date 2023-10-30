package com.dudenduke.hudalternatives.common;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SurvivalPlayerSnapshot {

        public enum Effect
        {
            NONE, POISONED, WITHERED, HUNGERED
        }

        public final LocalPlayer localPlayer;
        public final int experienceLevel;
        public final int totalExperience;
        public final float health;
        public final float maxHealth;
        public final float absorption;
        public final float foodLevel;
        public final float maxFoodLevel;
        public final float saturation;

        public final Effect healthEffect;
        public final Effect hungerEffect;

        public final float drownPercentage;

        public final int selectedHotbarIndex;
        public final ItemStack mainHandItem;

        private ItemStack[] _hotbar = null;


        public SurvivalPlayerSnapshot(LocalPlayer player) {
            localPlayer = player;

            experienceLevel = player.experienceLevel;
            totalExperience = player.totalExperience;

            maxHealth = player.getMaxHealth();
            health = player.getHealth();
            absorption = player.getAbsorptionAmount();
            final FoodData foodData = player.getFoodData();
            maxFoodLevel = 20f;
            foodLevel = foodData.getFoodLevel();
            saturation = foodData.getSaturationLevel();

            healthEffect = getHealthEffect(player);
            hungerEffect = getHungerEffect(player);

            mainHandItem = player.getMainHandItem();
            selectedHotbarIndex = player.getInventory().selected;

            drownPercentage = 1 - (((float)player.getAirSupply()) / ((float)player.getMaxAirSupply()));
        }


        public ItemStack[] getHotbar() {
            if (_hotbar != null) return _hotbar;
            ItemStack[] hotbar = new ItemStack[9];
            for (int i = 0; i < hotbar.length; i++) {
                hotbar[i] = localPlayer.getInventory().getItem(i);
            }
            _hotbar = hotbar;
            return  hotbar;
        }

        private static Effect getHealthEffect(LocalPlayer player) {
                final var effects = player.getActiveEffects();
                final var poisonedOrWitheredEffect = effects.stream()
                    .filter(e -> e.getEffect().equals(MobEffects.WITHER) || e.getEffect().equals(MobEffects.POISON))
                    .findFirst();

                return (poisonedOrWitheredEffect.isPresent())
                    ? (poisonedOrWitheredEffect.get().getEffect().equals(MobEffects.WITHER)
                        ? SurvivalPlayerSnapshot.Effect.WITHERED : SurvivalPlayerSnapshot.Effect.POISONED)
                    : SurvivalPlayerSnapshot.Effect.NONE;
        }

        private static Effect getHungerEffect(LocalPlayer player) {
                final var effects = player.getActiveEffects();
                final var hungeredEffect = effects.stream()
                    .filter(e -> e.getEffect().equals(MobEffects.HUNGER))
                    .findFirst();

                return (hungeredEffect.isPresent()) ? SurvivalPlayerSnapshot.Effect.HUNGERED : SurvivalPlayerSnapshot.Effect.NONE;
        }

    public int findCorrectToolInHotbar() {
        var player = localPlayer;
        if (player == null) return -1;

        var blockHit = player.pick(5.0d, 0.0f, false);
        if (blockHit.getType() != HitResult.Type.BLOCK) {
            return -1;
        }

        var blockPos = ((BlockHitResult)blockHit).getBlockPos();
        var blockState = player.getLevel().getBlockState(blockPos);

        var blockStateTags = blockState.getTags().toList();


        var itemClass = (blockStateTags.contains(BlockTags.MINEABLE_WITH_PICKAXE)) ? PickaxeItem.class
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_AXE)) ? AxeItem.class
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_SHOVEL)) ? ShovelItem.class
            : (blockStateTags.contains(BlockTags.MINEABLE_WITH_HOE)) ? HoeItem.class
            : Item.class;

        if (itemClass == Item.class) {
            return -1;
        }

        var hotbar = getHotbar();
        var toolIndex = -1;
        for (int i = 0; i < hotbar.length; i++) {
            if (hotbar[i].getItem().getClass() == itemClass) {
                toolIndex = i;
                break;
            }
        }
        return toolIndex;
    }

    public int findNextMeleeWeaponInHotbar() {
        return findNextWeaponOfTypeInHotbar(WeaponType.MELEE);
    }

    public int findNextRangedWeaponInHotbar() {
        return findNextWeaponOfTypeInHotbar(WeaponType.RANGED);
    }

    private int findNextWeaponOfTypeInHotbar(WeaponType weaponType) {
        var hotbar = getHotbar();
        var nextWeaponIndex = -1;

        int startingIndex = 0;
        if (weaponType == WeaponType.MELEE) {
            startingIndex = WeaponUtils.isMeleeWeapon(mainHandItem) ? selectedHotbarIndex + 1 : 0;
        }
        else if (weaponType == WeaponType.RANGED) {
            startingIndex = WeaponUtils.isRangedWeapon(mainHandItem) ? selectedHotbarIndex + 1 : 0;
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