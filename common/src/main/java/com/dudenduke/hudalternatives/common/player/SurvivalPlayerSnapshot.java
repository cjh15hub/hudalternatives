package com.dudenduke.hudalternatives.common.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

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

        int currentlySelectedHotbarIndex = player.getInventory().selected;
        if (SurvivalPlayerEvent.lastSelectedHotbarIndex() != -1 && currentlySelectedHotbarIndex != SurvivalPlayerEvent.lastSelectedHotbarIndex()) {
            SurvivalPlayerEvent.setLastItemSlotChangedInstant();
        }
        SurvivalPlayerEvent.setLastSelectedHotbarIndex(currentlySelectedHotbarIndex);
        selectedHotbarIndex = currentlySelectedHotbarIndex;

        drownPercentage = 1 - (((float)player.getAirSupply()) / ((float)player.getMaxAirSupply()));
    }

    private Effect getHealthEffect(LocalPlayer player) {
        final var effects = player.getActiveEffects();
        final var poisonedOrWitheredEffect = effects.stream()
                .filter(e -> e.getEffect().equals(MobEffects.WITHER) || e.getEffect().equals(MobEffects.POISON))
                .findFirst();

        return (poisonedOrWitheredEffect.isPresent())
                ? (poisonedOrWitheredEffect.get().getEffect().equals(MobEffects.WITHER)
                ? SurvivalPlayerSnapshot.Effect.WITHERED : SurvivalPlayerSnapshot.Effect.POISONED)
                : SurvivalPlayerSnapshot.Effect.NONE;
    }

    private Effect getHungerEffect(LocalPlayer player) {
        final var effects = player.getActiveEffects();
        final var hungeredEffect = effects.stream()
                .filter(e -> e.getEffect().equals(MobEffects.HUNGER))
                .findFirst();

        return (hungeredEffect.isPresent()) ? SurvivalPlayerSnapshot.Effect.HUNGERED : SurvivalPlayerSnapshot.Effect.NONE;
    }
}
