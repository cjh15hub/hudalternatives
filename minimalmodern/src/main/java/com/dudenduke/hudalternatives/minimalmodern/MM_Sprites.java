package com.dudenduke.hudalternatives.minimalmodern;
import com.dudenduke.hudalternatives.core.Dimensions;
import com.dudenduke.hudalternatives.core.Sprite;
import com.dudenduke.hudalternatives.core.Vector2;

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


    public static Sprite Horse_Silhouette = new Sprite(2, 23, 19, 20);
    public static Sprite Horse = new Sprite(2, 2, 19, 20);
    public static Sprite Donkey = new Sprite(24, 2, 19, 20);
    public static Sprite Pig = new Sprite(47, 2, 19, 20);
    public static Sprite Camel = new Sprite(69, 2, 20, 21);
    public static Sprite Skeleton_Horse = new Sprite(93, 2, 19, 20);
    public static Sprite Strider = new Sprite(115, 2, 20, 20);
    public static Sprite MountHealthBackground = new Sprite(59, 153, 59, 5);
    public static Sprite MountHealthBar = new Sprite(120, 154, 57, 3);
}
