package com.Util.Math;

public class Rect2 {
    private Vector2 pos;
    private Vector2 size;

    public Rect2(float x, float y, float width, float height) {
        this.pos = new Vector2(x, y);
        this.size = new Vector2(width, height);
    }

    public Rect2(Vector2 pos, Vector2 size) {
        this.pos = pos.clone();
        this.size = size.clone();
    }

    public Rect2(Vector2 addClone, float width, float height) {
        this.pos = addClone.clone();
        this.size = new Vector2(width, height);
    }

    public float getX() {
        return pos.getX();
    }

    public float getY() {
        return pos.getY();
    }

    public float getWidth() {
        return size.getX();
    }

    public float getHeight() {
        return size.getY();
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getEnd() {
        return pos.addClone(size);
    }

    // Returns true if the rectangle shares any points with the other rectangle.
    public boolean overlaps(Rect2 screenRect) {
        return !(screenRect.getX() > getX() + getWidth() || screenRect.getX() + screenRect.getWidth() < getX() || screenRect.getY() > getY() + getHeight() || screenRect.getY() + screenRect.getHeight() < getY());
    }

    public boolean overlapst(Rect2 screenRect) {
        System.out.println(screenRect.getY() + " " + screenRect.getHeight() + " " + getY() + " " + getHeight());
        return !(screenRect.getX() > getX() + getWidth() || screenRect.getX() + screenRect.getWidth() < getX() || screenRect.getY() > getY() + getHeight() || screenRect.getY() + screenRect.getHeight() < getY());
    }

    public String toString() {
        return "Rect2{" +
                "pos=" + pos +
                ", size=" + size +
                '}';
    }
}
