package com.Util.Math;

import java.util.Random;

public class Vector2 {

    public float x = 0;
    public float y = 0;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public String toString() {
        return (int) x + ", " + (int) y;
    }

    public String statistics() {
        return x + ", " + y;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;

        return new Vector2(x, y);
    }

    public Vector2 add(float dx, float dy) {
        this.x = x + dx;
        this.y = y + dy;

        return new Vector2(x, y);
    }


    public Vector2 subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;

        return new Vector2(x, y);
    }

    public Vector2 addClone(Vector2 other) {
        float xx = x + other.x;
        float yy = y + other.y;

        return new Vector2(xx, yy);
    }

    public Vector2 addClone(float dx, float dy) {
        float xx = x + dx;
        float yy = y + dy;

        return new Vector2(xx, yy);
    }

    public Vector2 subtractClone(Vector2 other) {
        float xx = x - other.x;
        float yy = y - other.y;

        return new Vector2(xx, yy);
    }

    public Vector2 subtractClone(float dx, float dy) {
        float xx = x - dx;
        float yy = y - dy;

        return new Vector2(xx, yy);
    }

    public int compareTo(Vector2 other) {
        boolean c1 = x > other.x;
        boolean c2 = y > other.y;
        boolean c3 = x == other.x;
        boolean c4 = y == other.y;

        return ((c1 ? 1 : -1) + (c2 ? 1 : -1) + (c3 ? 1 : 0) + (c4 ? 1 : 0)) / 2;

        /*
          Values:
          Equality => 0 points
          Greater Than => 1 points
          Less Than => -1 points
        */
    }

    public boolean equalTo(Vector2 compare) {
        return x == compare.x && y == compare.y;
    }

    public Vector2 scale(float scaleFactor) {
        this.x *= scaleFactor;
        this.y *= scaleFactor;

        return new Vector2(x, y);
    }

    public Vector2 scaleClone(Vector2 scaleFactor) {
        float xx = x * scaleFactor.x;
        float yy = y * scaleFactor.y;

        return new Vector2(xx, yy);
    }

    public Vector2 scaleClone(float scaleFactor) {
        float xx = x * scaleFactor;
        float yy = y * scaleFactor;

        return new Vector2(xx, yy);
    }

    public Vector2 offsetClone(float offset) {
        float xx = x + offset;
        float yy = y + offset;

        return new Vector2(xx, yy);
    }

    public void offset(float offset) {
        x += offset;
        y += offset;
    }

    public static Vector2 randomVector(float xRange, float yRange) {
        Random random = new Random();

        Vector2 offset = new Vector2(random.nextFloat() * xRange, random.nextFloat() * yRange);

        return offset;
    }

    public static Vector2 magnitudeDirection(Vector2 v1, Vector2 v2) {
        float dx = v2.x - v1.x;
        float dy = v2.y - v1.y;

        Vector2 result = new Vector2(dx, dy);

        boolean xy = (Math.abs(dx) > Math.abs(dy));

        float slope = xy ? 1 / dx : 1 / dy;

        result.scale(slope);

        float xx = result.x;
        float yy = result.y;

        result.x = Math.abs(xx) * Math.signum(dx);
        result.y = Math.abs(yy) * Math.signum(dy);

        return result;
    }

    public static Vector2 reverseMag(Vector2 v1, Vector2 v2) {
        float dx = v2.x - v1.x;
        float dy = v2.y - v1.y;

        Vector2 result = new Vector2(dx, dy);

        float slope = 0;

        boolean xy = (Math.abs(dx) < Math.abs(dy));

        slope = xy ? 1 / dx : 1 / dy;

        result.scale(slope);

        float xx = result.x;
        float yy = result.y;

        result.x = Math.abs(xx) * Math.signum(dx);
        result.y = Math.abs(yy) * Math.signum(dy);

        return result;
    }

    public static float distance(Vector2 v1, Vector2 v2) {
        double x = (v1.x - v2.x);
        double y = (v1.y - v2.y);

        x = Math.pow(x, 2);
        y = Math.pow(y, 2);

        double dist = x + y;

        dist = Math.sqrt(dist);

        return (float) dist;
    }

    public static Vector2 identity(float scale) {
        return new Vector2(scale, scale);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }
}