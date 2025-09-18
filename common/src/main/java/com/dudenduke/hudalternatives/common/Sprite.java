package com.dudenduke.hudalternatives.common;

public class Sprite {

    public Sprite(int u, int v, int width, int height) {
        _position = new Vector2(u, v);
        _dimensions = new Dimensions(width, height);
    }
    public Sprite (Vector2 pos, Dimensions dims) {
        _position = pos;
        _dimensions = dims;
    }

    private final Dimensions _dimensions;
    private final Vector2 _position;

    public int u() { return _position.x(); }
    public int v() { return _position.y(); }
    public  int width() { return _dimensions.width(); }
    public int height() { return _dimensions.height(); }
    public Dimensions dimensions() { return _dimensions; }
}
