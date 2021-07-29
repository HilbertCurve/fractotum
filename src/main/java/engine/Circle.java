package engine;

import fractal.Fractal;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Circle {
    private float radius = 1.0f;
    private Vector2f center;
    private Vector3f color;

    public Circle() {

    }

    public Circle(Vector2f center, float radius) {
        this.radius = radius;
        this.center = center;
        this.color = Fractal.DEFAULT_COLOR;
    }

    public Circle(Vector2f center, Vector2f point) {
        this.radius = center.distance(point);
        this.center = center;
    }

    public Circle(Vector2f center, float radius, Vector3f color) {
        this.radius = radius;
        this.center = center;
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector3f getColor() {
        return color;
    }
}
