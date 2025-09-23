package com.dudenduke.hudalternatives.deviljars;

import com.dudenduke.hudalternatives.common.numerics.Dimensions;
import com.dudenduke.hudalternatives.common.graphics.Sprite;
import com.dudenduke.hudalternatives.common.numerics.Vector2;

public class DJ_Sprites {

    public static Sprite FullSheet = new Sprite(0, 0, 500, 300);

    public static final Dimensions EmptyJarBackgroundDims = new Dimensions(72, 41);
    public static final Dimensions HealthManaDims = new Dimensions(64, 32);
    public static final Dimensions DragonDims = new Dimensions(45,  42);
    public static final Dimensions GolemDims = new Dimensions(41,  49);
    public static final Dimensions HotbarConnectorDims = new Dimensions(190, 33);

    public static final Sprite EmptyJar = new Sprite(new Vector2(6, 63), EmptyJarBackgroundDims);
    public static final Sprite MainHealthBar = new Sprite(new Vector2(10, 7), HealthManaDims);
    public static final Sprite PoisonedHealthBar = new Sprite(new Vector2(90, 7), HealthManaDims);
    public static final Sprite WitheredHealthBar = new Sprite(new Vector2(90, 68), HealthManaDims);
    public static final Sprite GoldenHealthBar = new Sprite(new Vector2(10, 180), HealthManaDims);

    public static final Sprite MainManaBar = new Sprite(new Vector2(10, 132), HealthManaDims);
    public static final Sprite HungeredManaBar = new Sprite(new Vector2(90, 132), HealthManaDims);

    public static final Sprite Dragon = new Sprite(new Vector2(202, 7), DragonDims);
    public static final Sprite Golem = new Sprite(new Vector2(207, 61), GolemDims);
    public static final Sprite HotbarConnector = new Sprite(new Vector2(35, 252), HotbarConnectorDims );

}
