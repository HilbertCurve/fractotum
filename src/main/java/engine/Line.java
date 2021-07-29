package engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static util.MyMath.lerp;

public class Line {
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;

    public Line(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

    public Line(Vector2f from, Vector2f to, Vector3f color) {
        this.from = from;
        this.to = to;
        this.color = color;
    }

    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }

    public Vector2f getStart() {
        return this.from;
    }

    public Vector2f getEnd() {
        return this.to;
    }

    public Vector3f getColor() {
        return color;
    }

    public float lengthSquared() {
        return new Vector2f(to).sub(from).lengthSquared();
    }

    public Line scale(Vector2f v, float amt) {
        this.getStart().x = lerp(v.x, this.getStart().x, amt);
        this.getStart().y = lerp(v.y, this.getStart().y, amt);
        this.getEnd().x = lerp(v.x, this.getEnd().x, amt);
        this.getEnd().y = lerp(v.y, this.getEnd().y, amt);

        return this;
    }
}
