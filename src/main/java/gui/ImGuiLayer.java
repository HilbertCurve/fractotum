package gui;

import engine.KeyListener;
import engine.Window;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

public class ImGuiLayer {
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

        ImGui.begin("Fractotum");

        int iters = Window.getScene().iterations[0];

        // Number of fractal-ization iterations
        if (ImGui.sliderInt("Number of Iterations", Window.getScene().iterations, 0, 16)) {
            iters = Window.getScene().iterations[0];
            Window.getScene().generateFractal(iters);
        }

        // Number of points to draw
        if (ImGui.sliderInt("Number of Vertices", Window.getScene().numPoints, 2, 8))
            Window.getScene().generateFractal(iters);

        ImGui.newLine();

        // Points to change
        for (int i = 0; i < Window.getScene().numPoints[0]; i++) {
            if (ImGui.sliderInt2("Vertex #" + i + " Sliders", Window.getScene().pointData[i], -100, 100)) {
                Window.getScene().generateFractal(iters);
            }
        }

        ImGui.end();
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
