package engine;

import fractal.Fractal;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.MyMath.rotate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Wrapper over all debug/physics-related draw calls.
 */
public class Renderer {
    static class CircleRenderer {
        public static final int MAX_CIRCLES = 10000;
        private static final int CIRCLE_VERTEX_COUNT = 102;

        public static final List<Circle> circles = new ArrayList<>();
        // 6 floats per vertex, 101 vertices per circle
        private static final float[] vertexArray = new float[MAX_CIRCLES * 6 * CIRCLE_VERTEX_COUNT];
        private static final Shader shader = AssetPool.getShader("src/main/resources/shaders/default.glsl");

        private static int vaoID;
        private static int vboID;

        private static boolean started = false;

        public static void start() {
            // Generate the vao
            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            // Create the vbo and buffer some memory
            vboID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

            // Enable the vertex array attributes
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
        }

        public static void beginFrame() {
            if (!started) {
                start();
                started = true;
            }
        }

        public static void draw() {
            if (circles.size() <= 0) return;

            int index = 0;
            for (Circle circle : circles) {
                Vector2f position = new Vector2f(circle.getCenter()).add(circle.getRadius(), 0);
                Vector2f center = circle.getCenter();
                Vector3f color = circle.getColor();

                // Load position
                index = loadVertex(index, position, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));

                for (int i = 0; i < CIRCLE_VERTEX_COUNT-2; i++) {
                    Vector2f v = new Vector2f(position);

                    rotate(center, v, 360f * (i)/(CIRCLE_VERTEX_COUNT-3));

                    index = loadVertex(index, v, new Vector4f(color, 0.4f));
                }

                index = loadVertex(index, center, new Vector4f(0.0f, 0.0f, 0.0f, 0.4f));
            }

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, circles.size() * 6 * CIRCLE_VERTEX_COUNT));

            // Use our shader
            shader.use();
            shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
            shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

            // Bind the vao
            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw the batch
            glDrawArrays(GL_LINE_STRIP, 0, circles.size() * CIRCLE_VERTEX_COUNT);

            // Disable Location
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Unbind shader
            shader.detach();
        }

        private static int loadVertex(int index, Vector2f vec, Vector4f color) {
            // Load position
            vertexArray[index] = vec.x;
            vertexArray[index + 1] = vec.y;

            // Load color
            vertexArray[index + 2] = color.x;
            vertexArray[index + 3] = color.y;
            vertexArray[index + 4] = color.z;
            vertexArray[index + 5] = color.w;

            index += 6;

            return index;
        }
    }

    static class LineRenderer {
        public static final int MAX_LINES = 500000;

        public static final List<Line> lines = new ArrayList<>();
        // 6 floats per vertex, 2 vertices per line
        private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
        private static final Shader shader = AssetPool.getShader("src/main/resources/shaders/default.glsl");

        private static int vaoID;
        private static int vboID;

        private static boolean started = false;

        public static void start() {
            // Generate the vao
            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            // Create the vbo and buffer some memory
            vboID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

            // Enable the vertex array attributes
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);

            glLineWidth(1.0f);
        }

        public static void beginFrame() {
            if (!started) {
                start();
                started = true;
            }
        }


        public static void draw() {
            if (lines.size() <= 0) return;

            int index = 0;
            for (Line line : lines) {
                for (int i=0; i < 2; i++) {
                    Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                    Vector3f color = line.getColor();

                    // Load position
                    vertexArray[index] = position.x;
                    vertexArray[index + 1] = position.y;
                    vertexArray[index + 2] = -10.0f;

                    // Load the color
                    vertexArray[index + 3] = color.x;
                    vertexArray[index + 4] = color.y;
                    vertexArray[index + 5] = color.z;
                    index += 6;
                }
            }

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

            // Use our shader
            shader.use();
            shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
            shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

            // Bind the vao
            glBindVertexArray(vaoID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw the batch
            glDrawArrays(GL_LINES, 0, lines.size() * 2);

            // Disable Location
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            // Unbind shader
            shader.detach();
        }
    }

    public static void start() {
        CircleRenderer.start();
        LineRenderer.start();
    }

    public static void beginFrame() {
        CircleRenderer.beginFrame();
        LineRenderer.beginFrame();
    }

    public static void draw() {
        CircleRenderer.draw();
        LineRenderer.draw();
    }

    ////////////////////
    /* CIRCLE-METHODS */
    ////////////////////
    public static void addCircle(Circle circle) {
        if (CircleRenderer.circles.size() >= CircleRenderer.MAX_CIRCLES) return;
        CircleRenderer.circles.add(circle);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        if (CircleRenderer.circles.size() >= CircleRenderer.MAX_CIRCLES) return;
        CircleRenderer.circles.add(new Circle(center, radius, color));
    }

    public static Circle getCircle(int index) {
        return CircleRenderer.circles.get(index);
    }

    //////////////////
    /* LINE-METHODS */
    //////////////////
    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        if (LineRenderer.lines.size() >= LineRenderer.MAX_LINES) return;
        LineRenderer.lines.add(new Line(from, to, color));
    }

    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, Fractal.DEFAULT_COLOR);
    }

    public static Line getLine(int index) {
        return LineRenderer.lines.get(index);
    }
}
