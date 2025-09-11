package com.dudenduke.hudalternatives.common;

public class Sprite {

    public Sprite(int u, int v, int width, int height) {
        position = new Vector2(u, v);
        dimensions = new Dimensions(width, height);
    }
    public Sprite (Vector2 pos, Dimensions dims) {
        position = pos;
        dimensions = dims;
    }

    private final Dimensions dimensions;
    private final Vector2 position;

    public int u() { return position.x(); }
    public int v() { return position.y(); }
    public  int width() { return dimensions.width(); }
    public int height() { return dimensions.height(); }

    public static Sprite SubSpriteWidth(Sprite source, int width) {
        return new Sprite(source.u(), source.v(), width, source.height());
    }

}
