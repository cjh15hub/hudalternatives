package com.dudenduke.hudalternatives.minimalmodern;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class MM_KeyBindings {
    public static final String KEY_CATEGORY_MM_MOD = "key.category.minimal_modern.main";

    public static final String KEY_FIND_MELEE_WEAPON = "key.minimal_modern.find_melee_weapon";
    public static final String KEY_FIND_RANGED_WEAPON = "key.minimal_modern.find_ranged_weapon";
    public static final String KEY_FIND_TOOL = "key.minimal_modern.find_tool";


    public static final KeyMapping FIND_MELEE_WEAPON_INPUT = new KeyMapping(KEY_FIND_MELEE_WEAPON, KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_MM_MOD);

    public static final KeyMapping FIND_RANGED_WEAPON_INPUT = new KeyMapping(KEY_FIND_RANGED_WEAPON, KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_MM_MOD);

    public static final KeyMapping FIND_TOOL_INPUT = new KeyMapping(KEY_FIND_TOOL, KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_MM_MOD);


    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(FIND_MELEE_WEAPON_INPUT);
        event.register(FIND_RANGED_WEAPON_INPUT);
        event.register(FIND_TOOL_INPUT);
    }
}
