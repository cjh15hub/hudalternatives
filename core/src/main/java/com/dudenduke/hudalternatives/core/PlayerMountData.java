package com.dudenduke.hudalternatives.core;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.time.Instant;
import java.util.List;

public class PlayerMountData {
    private static boolean isPlayerMounted = false;
    private static Instant whenPlayerMounted = null;
    private static LivingVehicleType mountType = null;
    private static float currentMountHealth = -1;
    private static float mountLastFrameHealth = -1;
    private static float mountMaxHealth = -1;
    private static Instant whenMountDamagedWhileRiding = null;


    public static boolean isPlayerMounted() { return isPlayerMounted; }
    public static boolean wasPreviouslyRidingMount() { return whenPlayerMounted != null; }
    public static Instant getWhenPlayerMounted() { return whenPlayerMounted; }
    public static LivingVehicleType getMountType() { return mountType; }
    public static float getMountHealth() { return currentMountHealth; }
    public static float getMountLastFrameHealth() { return mountLastFrameHealth; }
    public static float getMountMaxHealth() { return mountMaxHealth; }
    public static Instant getWhenMountDamagedWhileRiding() { return whenMountDamagedWhileRiding; }

    public static void updateMountData(LocalPlayer player) {
        var vehicle = player.getVehicle();

        if (vehicle instanceof LivingEntity mob) {
            isPlayerMounted = true;
            mountType = getMountType(mob);
            mountLastFrameHealth = currentMountHealth;
            currentMountHealth = mob.getHealth();
            mountMaxHealth = mob.getMaxHealth();

            // Started riding mount
            if ( !wasPreviouslyRidingMount()) {
                whenPlayerMounted = Instant.now();
            }

            // Mount was damaged while riding
            if (currentMountHealth < mountLastFrameHealth) {
                whenMountDamagedWhileRiding = Instant.now();
            }
        }
        else {
            isPlayerMounted = false;
            mountType = null;
            mountLastFrameHealth = -1;
            currentMountHealth = -1;
            mountMaxHealth = -1;
            whenPlayerMounted = null;
            whenMountDamagedWhileRiding = null;
        }
    }

    public static LivingVehicleType getMountType(LivingEntity mob) {
        var translationKey = mob.getName().toString();

        List<Pair<String, LivingVehicleType>> sourcesToEnumMap = List.of(
            Pair.of("entity.minecraft.horse", LivingVehicleType.Horse),
            Pair.of("entity.minecraft.camel", LivingVehicleType.Camel),
            Pair.of("entity.minecraft.donkey", LivingVehicleType.Donkey),
            Pair.of("entity.minecraft.mule", LivingVehicleType.Mule),
            Pair.of("entity.minecraft.skeleton_horse", LivingVehicleType.Skeleton_horse),
            Pair.of("entity.minecraft.strider", LivingVehicleType.Strider),
            Pair.of("entity.minecraft.pig", LivingVehicleType.Pig)
        );

        for (var pair: sourcesToEnumMap) {
            if (translationKey.contains(pair.getFirst())) {
                return pair.getSecond();
            }
        }

        return LivingVehicleType.Unknown;
    }
}


