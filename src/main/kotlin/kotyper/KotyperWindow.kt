package kotyper

import imgui.ImGui
import imgui.ImGuiIO
import imgui.app.Application
import imgui.app.Configuration
import imgui.flag.ImGuiConfigFlags
import org.lwjgl.glfw.GLFW

object KotyperWindow : Application() {
    val height = 600
    val width = 800

    private fun charCallback(window: Long, c: Int) {
        KotyperGame.charCallback(window, c)
    }

    private fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {
        KotyperGame.keyCallback(window, key, scancode, action, mode)
    }

    override fun process() {
        KotyperGame.drawGui()
    }

    override fun configure(config: Configuration) {
        config.title = "Example Application"
        config.height = height
        config.width = width
    }

    override fun initImGui(config: Configuration?) {
        super.initImGui(config)
        val io: ImGuiIO = ImGui.getIO()

        io.iniFilename = null // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard) // Enable Keyboard Controls

        io.fontGlobalScale = 1.66f;
        io.getFonts().addFontDefault()

        GLFW.glfwSetCharCallback(super.handle, this::charCallback)
        GLFW.glfwSetKeyCallback(super.handle, this::keyCallback)
    }
}