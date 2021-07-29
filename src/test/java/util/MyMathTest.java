package util;

import org.joml.Vector2f;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MyMathTest {
    @Test
    public void lerpTestShouldReturnTrue() {
        Vector2f v = new Vector2f(1.0f, 1.0f);
        Vector2f b = new Vector2f(0.0f, 0.0f);

        Vector2f lerp = new Vector2f(MyMath.lerp(v.x, b.x, 0.32f), MyMath.lerp(v.y, b.y, 0.32f));

        assertTrue(lerp.equals(0.68f, 0.68f));
    }
}
