package com.dudenduke.hudalternatives.minimalmodern;

import net.minecraftforge.common.ForgeConfigSpec;

public class MM_Configuration {
    public static final ForgeConfigSpec.ConfigValue<Integer> MAIN_GUI_DRAW_CORNER;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAIN_GUI_HORIZONTAL_PADDING;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAIN_GUI_VERTICAL_PADDING;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SEPARATE_VALUE_BARS;
    public static final ForgeConfigSpec.ConfigValue<Integer> VALUE_BARS_DRAW_CORNER;
    public static final ForgeConfigSpec.ConfigValue<Integer> VALUE_BARS_HORIZONTAL_PADDING;
    public static final ForgeConfigSpec.ConfigValue<Integer> VALUE_BARS_VERTICAL_PADDING;


    public static int MainGuiDrawCorner() { return MAIN_GUI_DRAW_CORNER.get(); }
    public static int MainGuiHorizontalPadding() { return MAIN_GUI_HORIZONTAL_PADDING.get(); }
    public static int MainGuiVerticalPadding() { return MAIN_GUI_VERTICAL_PADDING.get(); }


    public static boolean SeparateValueBars() { return SEPARATE_VALUE_BARS.get(); }
    public static int ValueBarsDrawCorner() { return VALUE_BARS_DRAW_CORNER.get(); }
    public static int ValueBarsHorizontalPadding() { return VALUE_BARS_HORIZONTAL_PADDING.get(); }
    public static int ValueBarsVerticalPadding() { return VALUE_BARS_VERTICAL_PADDING.get(); }


    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Minimal Modern");

        MAIN_GUI_DRAW_CORNER = BUILDER.comment("Which corner to display main GUI? 0 = TopLeft, 1 = TopRight, 2 = BottomLeft, 3 = BottomRight")
                .defineInRange("MainGuiCorner", 2, 0, 3);

        MAIN_GUI_HORIZONTAL_PADDING = BUILDER.comment("Horizontal Padding for main GUI.")
            .defineInRange("MainGuiHorizontalPadding", 0, 0, 50);

        MAIN_GUI_VERTICAL_PADDING = BUILDER.comment("Vertical Padding for main GUI.")
            .defineInRange("MainGuiVerticalPadding", 0, 0, 50);


        SEPARATE_VALUE_BARS = BUILDER.comment(
            "Separate the main GUI from the Value Bars.",
            "The following settings will only take effect if set to true",
            "The corner values must also be different."
        ).define("SeparateValueBars", false);

        VALUE_BARS_DRAW_CORNER = BUILDER.comment("Which corner to display Value Bars? 0 = TopLeft, 1 = TopRight, 2 = BottomLeft, 3 = BottomRight")
            .defineInRange("ValueBarsCorner", 2, 0, 3);

        VALUE_BARS_HORIZONTAL_PADDING = BUILDER.comment("Horizontal Padding for Value Bars.")
            .defineInRange("ValueBarsHorizontalPadding", 0, 0, 50);

        VALUE_BARS_VERTICAL_PADDING = BUILDER.comment("Vertical Padding for Value Bars.")
            .defineInRange("ValueBarsVerticalPadding", 0, 0, 50);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
