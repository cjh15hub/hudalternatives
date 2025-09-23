package com.dudenduke.hudalternatives.common.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.time.Instant;

public class PlayerMountEvent {
    private PlayerMountEvent() {}

    // TODO: check for events for these
    private static Instant _whenPlayerMounted = null;
    private static Instant _whenMountDamagedWhileRiding = null;
    private static float _mountLastFrameHealth = -1;

    public static void UpdatePlayerMountEvent(LocalPlayer player) {
        if (player.getVehicle() instanceof LivingEntity mount) {
            // Started riding mount
            if (PlayerMountEvent.whenPlayerMounted() == null) {
                PlayerMountEvent.setWhenPlayerMounted();
                PlayerMountEvent.clearWhenMountDamagedWhileRiding();
            }
            // Mount was damaged while riding
            else if (mount.getHealth() < PlayerMountEvent.mountLastFrameHealth()) {
                PlayerMountEvent.setWhenMountDamagedWhileRiding();
            }
            PlayerMountEvent.setMountLastFrameHealth(mount.getHealth());
        }
        else {
            PlayerMountEvent.clearMountLastFrameHealth();
            PlayerMountEvent.clearWhenPlayerMounted();
            PlayerMountEvent.clearWhenMountDamagedWhileRiding();
        }
    }

    public static Instant whenPlayerMounted() {
        return _whenPlayerMounted;
    }

    public static void setWhenPlayerMounted() {
        _whenPlayerMounted = Instant.now();
    }

    public static void setWhenPlayerMounted(Instant instant) {
        _whenPlayerMounted = instant;
    }

    public static void clearWhenPlayerMounted() {
        _whenPlayerMounted = null;
    }


    public static Instant whenMountDamagedWhileRiding() {
        return _whenMountDamagedWhileRiding;
    }

    public static void setWhenMountDamagedWhileRiding() {
        _whenMountDamagedWhileRiding = Instant.now();
    }

    public static void setWhenMountDamagedWhileRiding(Instant instant) {
        _whenMountDamagedWhileRiding = instant;
    }

    public static void clearWhenMountDamagedWhileRiding() {
        _whenMountDamagedWhileRiding = null;
    }


    public static float mountLastFrameHealth() {
        return _mountLastFrameHealth;
    }

    public static void setMountLastFrameHealth(float health) {
        _mountLastFrameHealth = health;
    }

    public static void clearMountLastFrameHealth() {
        _mountLastFrameHealth = -1;
    }
}
