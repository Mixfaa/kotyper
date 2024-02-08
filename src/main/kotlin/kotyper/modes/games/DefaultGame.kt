package kotyper.modes.games

import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import kotyper.Gamestate
import kotyper.KotyperGameManager
import kotyper.KotyperImGui
import kotyper.dictionary.KotyperDictionariesManager
import kotyper.modes.KotyperGameResult
import kotyper.utils.NanoStopwatch
import kotlin.time.DurationUnit

class DefaultGame : KotyperGame<DefaultGame.GameResult>("Default Game") {
    private var targetText = ""
    private var typedTextBuilder = StringBuilder()
    private val wordsToGenerate = ImInt(5)
    private val nanoStopwatch = NanoStopwatch()
    override fun drawGame() {
        KotyperImGui.beginDefaultGameWindow()
        //KotyperImGui.drawTypingText(targetText, typedTextBuilder.toString())
        KotyperImGui.drawTypingText2(targetText, typedTextBuilder.toString())
        KotyperImGui.endDefaultGameWindow()

        if (targetText.contentEquals(typedTextBuilder.toString())) {

            nanoStopwatch.stop()

            val result = GameResult(targetText, nanoStopwatch.convertTo(DurationUnit.SECONDS).toInt())
            results.add(result)

            KotyperGameManager.currentState = Gamestate.AFTER_GAME
            typedTextBuilder.clear()
            targetText = ""
        }
    }

    override fun drawAfterGameWindow() {
        val lastResult = results.last()

        ImGui.begin("Results", ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.AlwaysAutoResize)

        ImGui.text("Your WPM: ${lastResult.getWPM()}")
        ImGui.text("Your CPS: ${lastResult.getCPS()}")
        ImGui.text("Time: ")
        ImGui.sameLine()
        ImGui.text(lastResult.timeInSec.toString())

        ImGui.separator()
        KotyperImGui.textCentered("Press space to continue")
        if (ImGui.isKeyReleased(32))
            KotyperGameManager.currentState = Gamestate.IDLE


        ImGui.end()
    }

    override fun drawPreGameGui() {
        ImGui.inputInt("Words to generate", wordsToGenerate)

        ImGui.text("to start game press this button:")

        if (ImGui.button("start game or press space") || ImGui.isKeyReleased(32)) {
            KotyperGameManager.currentState = Gamestate.PLAYING
            targetText = KotyperDictionariesManager.currentDictionary?.getRandomText(wordsToGenerate.get())!!
            nanoStopwatch.start()
        }
    }


    override fun charCallback(window: Long, c: Int) {
        typedTextBuilder.append(c.toChar())

        val typedText = typedTextBuilder.toString()

        if (typedText.length < targetText.length)
            if (typedText.last() != targetText[typedText.lastIndex])
                KotyperGameManager.errorSound.restart()
    }

    override fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {
        if (action == 1 && key == 259 && typedTextBuilder.isNotEmpty())
            typedTextBuilder.deleteCharAt(typedTextBuilder.lastIndex)
    }

    override fun drawResults() {
        for ((index, result) in results.withIndex()) {
            ImGui.text("Time: ${result.timeInSec} WPM: ${result.getWPM()}")
            ImGui.sameLine()
            if (ImGui.button("Delete###delres${index}"))
            {
                results.removeAt(index)
                return
            }
        }
    }

    override fun onGameStop() {
        typedTextBuilder.clear()
        targetText = ""
    }

    class GameResult() : KotyperGameResult() {

        var text: String = ""
        var timeInSec: Int = 0

        constructor(text: String, timeInSec: Int) : this() {
            this.text = text
            this.timeInSec = timeInSec
        }

        override fun getCPS(): Double {
            return (text.length / timeInSec.toDouble())
        }
    }
}