package com.dudenduke.hudalternatives;

import com.dudenduke.hudalternatives.minimalmodern.MM_Configuration;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MODID)
public class HudAlternativesMod
{
    public static final Logger LOGGER = LogUtils.getLogger();

    public HudAlternativesMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        if (Constants.ModBuild == ModBuildCase.MINIMAL_MODERN) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MM_Configuration.SPEC, "minimal_modern.toml");
        }
    }

}
