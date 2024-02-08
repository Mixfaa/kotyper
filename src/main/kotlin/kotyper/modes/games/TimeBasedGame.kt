package kotyper.modes.games

import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import kotyper.Gamestate
import kotyper.KotyperGameManager
import kotyper.KotyperImGui
import kotyper.dictionary.KotyperDictionariesManager
import kotyper.modes.KotyperGameResult
import kotyper.utils.SimpleTimer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class TimeBasedGame : KotyperGame<TimeBasedGame.Result>("Time based game") {
    private var targetText = ""
    private var typedTextBuilder = StringBuilder()
    private var timeLimit = ImInt(30)
    private val simpleTimer = SimpleTimer()

    @OptIn(ExperimentalTime::class)
    override fun drawGame() {

        if (simpleTimer.isExpired()) {
            onTimerExpire()
            return
        }

        KotyperImGui.beginDefaultGameWindow()


        ImGui.text(
            "${
                Duration.convert(simpleTimer.getRest().toDouble(), DurationUnit.MILLISECONDS, DurationUnit.SECONDS)
                    .toInt()
            }"
        )

        ImGui.beginChild("typing text", 0f, 0f, false, ImGuiWindowFlags.NoDecoration)
        KotyperImGui.drawTypingText(targetText, typedTextBuilder.toString())
        ImGui.endChild()
        KotyperImGui.endDefaultGameWindow()

        if (targetText.contentEquals(typedTextBuilder.toString())) {
            KotyperGameManager.currentState = Gamestate.AFTER_GAME
            typedTextBuilder.clear()
            targetText = ""

            simpleTimer.start(timeLimit.toDouble(), DurationUnit.SECONDS)
        }
    }

    private fun onTimerExpire() {

        val result = Result(typedTextBuilder.toString(), targetText, timeLimit.get())
        results.add(result)

        KotyperGameManager.currentState = Gamestate.AFTER_GAME
        typedTextBuilder.clear()
        targetText = ""
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
        ImGui.inputInt("Time Limit in seconds", timeLimit)

        ImGui.text("Press space when you are ready")

        if (ImGui.isKeyReleased(32)) {
            KotyperGameManager.currentState = Gamestate.PLAYING
            targetText = KotyperDictionariesManager.currentDictionary?.getRandomText(20)!!

            simpleTimer.start(timeLimit.toDouble(), DurationUnit.SECONDS)
        }
    }

    override fun charCallback(window: Long, c: Int) {
        typedTextBuilder.append(c.toChar())

        if (c == ' '.code) {
            val typedWords = typedTextBuilder.count { chr -> chr == ' ' }
            val textWords = targetText.count { chr -> chr == ' ' }

            if (textWords - typedWords < 5)
                targetText += KotyperDictionariesManager.currentDictionary?.getRandomText(10)
        }

        val typedText = typedTextBuilder.toString()

        if (typedText.length < targetText.length && typedText.last() != targetText[typedText.lastIndex])
            KotyperGameManager.errorSound.restart()
    }

    override fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {
        if (action == 1 && key == 259 && typedTextBuilder.isNotEmpty()) typedTextBuilder.deleteCharAt(typedTextBuilder.lastIndex)
    }

    override fun drawResults() {
        for ((index, result) in results.withIndex()) {
            ImGui.text("Timeout: ${result.timeInSec} WPM: ${result.getWPM()} CPS: ${result.getCPS()}")
            ImGui.sameLine()
            if (ImGui.button("Delete###delres${index}")) {
                results.removeAt(index)
                return
            }
        }
    }

    override fun onGameStop() {
        typedTextBuilder.clear()
        targetText = ""
    }

    class Result() : KotyperGameResult() {

        private var typedText: String = ""
        private var targetText: String = ""
        var timeInSec: Int = 0
        private val rightChars: Int
            get() {
                val typedWords = typedText.split(' ')
                val targetWords = targetText.split(' ')

                var charsCount = 0

                for ((index, word) in targetWords.withIndex()) {

                    if (index > typedWords.lastIndex)
                        break

                    if (word.startsWith(typedWords[index])) {
                        charsCount += word.length
                    }
                }
                return charsCount
            }

        constructor(typedText: String, targetText: String, timeInSec: Int) : this() {
            this.typedText = typedText
            this.targetText = targetText
            this.timeInSec = timeInSec
        }


        override fun getCPS(): Double {
            return rightChars / timeInSec.toDouble();
        }

    }

}