package gui;

import engine.Window;
import fractal.Fractal;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ImGuiLayer {
    public static final int MAX_ITERATIONS = 16;
    public static final int MAX_VERTICES = 8;

    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public void init(long glfwWindow) {
        ImGui.createContext();

        imGuiGl3.init("#version 330");
        imGuiGlfw.init(glfwWindow, false);

        final ImGuiIO io = ImGui.getIO();
        io.setConfigViewportsNoTaskBarIcon(true);
    }

    public void newFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        List<Fractal> fractals = Window.getScene().getFractals();

        for (int j = 0; j < fractals.size(); j++) {
            Fractal f = fractals.get(j);
            ImGui.begin("Fractal #" + j);

            // Number of fractal-ization iterations
            if (ImGui.sliderInt("Number of Iterations", f.getIterations(), 0, MAX_ITERATIONS)) {
                Window.getScene().updateFractal(f, f.getIterations()[0]);
            }

            // Number of points to draw
            if (ImGui.sliderInt("Number of Vertices", f.getNumPoints(), 2, MAX_VERTICES)) {
                Window.getScene().updateFractal(f, f.getIterations()[0]);
            }

            ImGui.newLine();

            // Points to change
            for (int i = 0; i < f.getNumPoints()[0]; i++) {
                if (ImGui.sliderInt2("Vertex #" + i + " Sliders", f.getBaseData()[i], -100, 100)) {
                    Window.getScene().updateFractal(f, f.getIterations()[0]);
                }
            }

            ImGui.end();
        }
    }

    public void render() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
