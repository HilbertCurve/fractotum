package engine;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;

    public Transform(Vector2f position) {
        this.position = position;
        this.scale = new Vector2f();
    }
}
