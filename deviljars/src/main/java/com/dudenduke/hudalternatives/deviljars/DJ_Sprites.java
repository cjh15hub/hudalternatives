package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.common.Dimensions;
import com.dudenduke.hudalternatives.common.Sprite;
import com.dudenduke.hudalternatives.common.Vector2;

public class DJ_Sprites {

    public static Sprite FullSheet = new Sprite(0, 0, 500, 300);

    public static final Dimensions EmptyJarBackgroundDims = new Dimensions(72, 41);
    public static final Dimensions HealthManaDims = new Dimensions(64, 32);
    public static final Dimensions DragonDims = new Dimensions(45,  42);
    public static final Dimensions GolemDims = new Dimensions(41,  49);

    public static final Sprite EmptyJar = new Sprite(new Vector2(6, 63), EmptyJarBackgroundDims);
    public static final Sprite MainHealthBar = new Sprite(new Vector2(10, 7), HealthManaDims);
    public static final Sprite MainManaBar = new Sprite(new Vector2(10, 132), HealthManaDims);
    public static final Sprite Dragon = new Sprite(new Vector2(117, 7), DragonDims);
    public static final Sprite Golem = new Sprite(new Vector2(122, 61), GolemDims);

    // health uv  10, 7   - uv1   74, 39
    // empty  uv  6, 63   - uv1   78, 104
    // mana   uv  10, 132 - uv1   74, 164
    // dragon uv  117, 7  - uv1   162, 49
    // golem  uv  122, 61 - uv1   163, 110

//    public static Sprite PoisonedHealthBar = new Sprite(new Vector2(60, 180), HealthManaDims);
//    public static Sprite WitheredHealthBar = new Sprite(new Vector2(60, 174), HealthManaDims);
//    public static Sprite GoldenHealthBar = new Sprite(60, 192, healthSpriteDims.width(), 3);

//    public static Sprite PoisonedHungerBar = new Sprite(new Vector2(60, 200), hungerSpriteDims);
//    public static Sprite SaturationHungerBar = new Sprite(60, 204, hungerSpriteDims.width(), 1);
}
