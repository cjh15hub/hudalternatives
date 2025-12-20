package com.dudenduke.hudalternatives.minimalmodern;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Constants.MODID)
public class HudAlternativesMod {

    public static final Logger LOGGER = LogUtils.getLogger();

    public HudAlternativesMod() {
        ModLoadingContext.get().registerConfig(
            ModConfig.Type.CLIENT,
            MM_Configuration.SPEC,
            "minimal_modern.toml"
        );
    }
}
