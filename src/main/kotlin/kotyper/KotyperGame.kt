package kotyper


import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import kotyper.dictionary.DictionaryLoader
import kotyper.utils.AudioPlayer
import kotyper.utils.KotyperImGui
import kotyper.utils.NanoTimer
import kotlin.time.DurationUnit

object KotyperGame {
    private var currentState = Gamestate.IDLE
    private var textToType: String = ""
    private var typedTextBuilder: StringBuilder = StringBuilder()

    private var wordsToGenerate: ImInt = ImInt(5)
    private var nanoTimer: NanoTimer = NanoTimer()
    private var prevTime: Double = 0.0

    private var errorSound = AudioPlayer("/error.wav")


    fun charCallback(window: Long, c: Int) {
        if (currentState == Gamestate.PLAYING) {
            typedTextBuilder.append(c.toChar())

            val typedText = typedTextBuilder.toString()

            if (typedText.length < textToType.length)
                if (typedText.last() != textToType[typedText.lastIndex])
                    errorSound.restart()
        }
    }

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {

        if (currentState == Gamestate.PLAYING) {
            if (action == 1 && key == 259 && typedTextBuilder.isNotEmpty())
                typedTextBuilder.deleteCharAt(typedTextBuilder.lastIndex)
        }
    }

    fun drawGui() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(KotyperWindow.width.toFloat(), KotyperWindow.height.toFloat())

        ImGui.begin(
            "Kotyper main window",
            ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.NoResize
        )

        if (currentState == Gamestate.IDLE) {

            ImGui.inputInt("Words to generate", wordsToGenerate)

            ImGui.text("to start game press this button:")

            if (ImGui.button("start game or press space") || ImGui.isKeyReleased(32)) {
                currentState = Gamestate.PLAYING
                textToType = DictionaryLoader.dictionaries["english"]?.getRandomText(wordsToGenerate.get())!!
                nanoTimer.start()
            }
        }

        if (currentState == Gamestate.PLAYING) {
            KotyperImGui.displayTextToType(textToType, typedTextBuilder.toString())

            if (textToType.contentEquals(typedTextBuilder.toString())) {
                currentState = Gamestate.AFTER_GAME
                typedTextBuilder.clear()
                textToType = ""

                nanoTimer.stop()

                prevTime = nanoTimer.convertTo(DurationUnit.SECONDS)
                println(prevTime)
            }
        }

        if (currentState == Gamestate.AFTER_GAME) {
            ImGui.text("You typed it for $prevTime seconds")
            if (ImGui.isKeyReleased(32))
                currentState = Gamestate.IDLE
        }

        ImGui.end()
    }

}