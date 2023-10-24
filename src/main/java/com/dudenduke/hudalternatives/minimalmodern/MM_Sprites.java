package com.dudenduke.hudalternatives.minimalmodern;
import com.dudenduke.hudalternatives.utils.Dimensions;
import com.dudenduke.hudalternatives.utils.Sprite;
import com.dudenduke.hudalternatives.utils.Vector2;

public class MM_Sprites {

    public static Sprite FullSheet = new Sprite(0, 0, 256, 256);

    public static Sprite LargeHex = new Sprite(28, 207, 32, 36);
    public static Sprite SmallHex = new Sprite(4, 194, 22, 24);
    public static Sprite ValueBarsBackground = new Sprite(59, 218, 76, 13);
    public static Sprite DrowingHexSprite = new Sprite(29, 172,  30, 34);


    private static final Dimensions healthSpriteDims = new Dimensions(74, 5);
    public static Sprite MainHealthBar = new Sprite(new Vector2(60, 186), healthSpriteDims);
    public static Sprite PoisonedHealthBar = new Sprite(new Vector2(60, 180), healthSpriteDims);
    public static Sprite WitheredHealthBar = new Sprite(new Vector2(60, 174), healthSpriteDims);
    public static Sprite GoldenHealthBar = new Sprite(60, 192, healthSpriteDims.width(), 3);


    private static final Dimensions hungerSpriteDims = new Dimensions(60, 3);
    public static Sprite MainHungerBar = new Sprite(new Vector2(60, 196), hungerSpriteDims);
    public static Sprite PoisonedHungerBar = new Sprite(new Vector2(60, 200), hungerSpriteDims);
    public static Sprite SaturationHungerBar = new Sprite(60, 204, hungerSpriteDims.width(), 1);
}