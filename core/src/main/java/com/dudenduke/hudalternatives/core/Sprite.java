package com.dudenduke.hudalternatives.core;

public class Sprite {

    public Sprite(int x, int y, int width, int height) {
        position = new Vector2(x, y);
        dimensions = new Dimensions(width, height);
    }
    public Sprite (Vector2 pos, Dimensions dims) {
        position = pos;
        dimensions = dims;
    }

    private final Dimensions dimensions;
    private final Vector2 position;

    public int x() { return position.x(); }
    public int y() { return position.y(); }
    public  int width() { return dimensions.width(); }
    public int height() { return dimensions.height(); }
}
