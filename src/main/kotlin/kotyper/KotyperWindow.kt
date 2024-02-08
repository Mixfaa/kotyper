package kotyper


import imgui.ImGui
import imgui.ImGuiIO
import imgui.app.Application
import imgui.app.Configuration
import imgui.flag.ImGuiConfigFlags
import org.lwjgl.glfw.GLFW

object KotyperWindow : Application() {
    const val HEIGHT = 600
    const val WIDTH = 800

    private fun charCallback(window: Long, c: Int) {
        KotyperGameManager.charCallback(window, c)
    }

    private fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {
        KotyperGameManager.keyCallback(window, key, scancode, action, mode)
    }

    override fun process() {
        KotyperGameManager.drawGui()
    }

    override fun configure(config: Configuration) {
        config.title = "Kotyper"
        config.height = HEIGHT
        config.width = WIDTH
    }

    override fun initImGui(config: Configuration?) {
        super.initImGui(config)
        val io: ImGuiIO = ImGui.getIO()

        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard) // Enable Keyboard Controls

        io.fontGlobalScale = 1.66f;
        io.getFonts().addFontDefault()

        GLFW.glfwSetCharCallback(super.handle, this::charCallback)
        GLFW.glfwSetKeyCallback(super.handle, this::keyCallback)
    }
    override fun preRun() {
        super.preRun()
        KotyperGameManager.onStartup()
    }

    override fun postRun() {
        super.postRun()
        KotyperGameManager.onClose()
    }

}
