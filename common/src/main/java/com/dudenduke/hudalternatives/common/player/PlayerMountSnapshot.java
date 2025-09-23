package com.dudenduke.hudalternatives.common.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.Map;

public class PlayerMountSnapshot {
    private final LivingEntity mount;

    private static final String _lookupKeyKey = "key='";
    private static final Map<String, LivingVehicleType> _sourceToEnumMap = Map.of(
        "entity.minecraft.horse", LivingVehicleType.Horse,
        "entity.minecraft.camel", LivingVehicleType.Camel,
        "entity.minecraft.donkey", LivingVehicleType.Donkey,
        "entity.minecraft.mule", LivingVehicleType.Mule,
        "entity.minecraft.skeleton_horse", LivingVehicleType.Skeleton_horse,
        "entity.minecraft.strider", LivingVehicleType.Strider,
        "entity.minecraft.pig", LivingVehicleType.Pig
    );

    public PlayerMountSnapshot(LocalPlayer player) {
        if (player.getVehicle() instanceof LivingEntity mount) {
            this.mount = mount;
        }
        else {
            mount = null;
        }
    }

    public boolean isPlayerMounted() {
        return mount != null;
    }

    public LivingVehicleType getMountType() {
        if (mount == null) return null;
        return getMountType(mount);
    }

    public float getMountHealth() {
        if (mount == null) return -1;
        return mount.getHealth();
    }

    public float getMountMaxHealth() {
        if (mount == null) return -1;
        return mount.getMaxHealth();
    }

    private LivingVehicleType getMountType(LivingEntity mob) {
        // extract key from component, because key is private
        final String componentKey = mob.getName().toString();
        int keyEqualsIndex = componentKey.indexOf(_lookupKeyKey) + _lookupKeyKey.length();
        int keyEndIndex = componentKey.indexOf("'", keyEqualsIndex);
        String translationKey = componentKey.substring(keyEqualsIndex, keyEndIndex);

        return _sourceToEnumMap.getOrDefault(translationKey, LivingVehicleType.Unknown);
    }
}
