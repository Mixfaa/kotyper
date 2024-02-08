package kotyper

import imgui.ImGui
import imgui.flag.ImGuiWindowFlags

object KotyperImGui {
    private fun displayUnderline() {
        val prevCursorPos = ImGui.getCursorPos()

        ImGui.setCursorPosY(prevCursorPos.y + 5f)

        ImGui.setScrollHereY()
        ImGui.textDisabled("_")

        ImGui.setCursorPosX(prevCursorPos.x)
        ImGui.setCursorPosY(prevCursorPos.y)
    }

    fun beginDefaultGameWindow(name: String = "Game window") {
        ImGui.setNextWindowSize(600f, 150f)
        ImGui.setNextWindowFocus()
        ImGui.begin(name, ImGuiWindowFlags.NoDecoration)
    }

    fun endDefaultGameWindow() = ImGui.end()

    fun textColoredEachChar(r: Int, g: Int, b: Int, a: Int, text: String) {
        for ((i, c) in text.withIndex()) {
            ImGui.textColored(r, g, b, a, c.toString())

            if (i != text.lastIndex)
                ImGui.sameLine(0f, 0f)
        }
    }

    fun drawTypingText2(targetText: String, typedText: String) {
        val wordsToType = targetText.split(' ')
        val typedWords = typedText.split(' ')

        for ((index, word) in wordsToType.withIndex()) {

            val wordWidth = ImGui.calcTextSize(word).x

            if (ImGui.getContentRegionAvailX() < wordWidth)
                ImGui.newLine()

            if (index == typedWords.lastIndex) {

                val widthToUnderline = ImGui.calcTextSize(typedWords[index]).x
                val cursorPosX = ImGui.getCursorPosX()

                ImGui.setCursorPosX(cursorPosX + widthToUnderline)
                displayUnderline()
                ImGui.setCursorPosX(cursorPosX)
            }

            if (index <= typedWords.lastIndex) {

                if (typedWords[index].isEmpty())
                    textColoredEachChar(60, 60, 60, 255, word)
                else if (typedWords[index].contentEquals(word))
                    textColoredEachChar(0, 255, 0, 255, word)
                else {
                    val typedWord = typedWords[index]

                    for ((cIndex, typedChar) in typedWord.withIndex()) {

                        if (cIndex <= word.lastIndex) {

                            if (word[cIndex] != typedChar)
                                ImGui.textColored(255, 0, 0, 255, word[cIndex].toString())
                            else
                                ImGui.textColored(0, 255, 0, 255, typedChar.toString())

                            if (cIndex == typedWord.lastIndex && cIndex < word.lastIndex) {
                                ImGui.sameLine(0f, 0f)
                                textColoredEachChar(60, 60, 60, 255, word.substring(cIndex + 1))
                            }
                        } else {
                            ImGui.textColored(255, 0, 0, 255, typedChar.toString())
                        }
                        ImGui.sameLine(0f, 0f)
                    }
                }
            } else {
                textColoredEachChar(60, 60, 60, 255, word)
            }

            ImGui.sameLine(0f, 0f)
            ImGui.text(" ")
            ImGui.sameLine(0f, 0f)
        }

        ImGui.newLine()

    }

    fun drawTypingText(textToType: String, typedText: String) {

        for ((index, symbol) in textToType.withIndex()) {

            if (symbol == ' ' && index != textToType.lastIndex) {

                val nextSpaceIndex = textToType.indexOf(' ', index + 1)

                if (nextSpaceIndex != -1) {

                    val tillNextSpaceWidth = ImGui.calcTextSize(textToType.substring(index + 1, nextSpaceIndex)).x
                    val cursorX = ImGui.getCursorPosX()

                    if (ImGui.getWindowContentRegionMaxX() < cursorX + tillNextSpaceWidth) {
                        ImGui.newLine()
                        continue
                    }
                }
            }

            var charDisplayed = false

            if (index >= typedText.length) {

                if (index == typedText.length)
                    displayUnderline()

                ImGui.textColored(60, 60, 60, 255, symbol.toString())
                charDisplayed = true
            }

            if (!charDisplayed) {
                val typedSymbol = typedText[index]

                if (typedSymbol == symbol)
                    ImGui.textColored(0, 255, 0, 255, symbol.toString())
                else
                    ImGui.textColored(255, 0, 0, 255, if (symbol == ' ') typedSymbol.toString() else symbol.toString())

            }

            ImGui.sameLine(0f, 0f)

            if (index == textToType.lastIndex)
                for (i in index + 1..<typedText.length) {

                    ImGui.textColored(255, 255, 0, 255, typedText[i].toString())
                    ImGui.sameLine(0f, 0f)

                    if (i == typedText.lastIndex)
                        displayUnderline()
                }
        }

        ImGui.newLine()
    }

    fun textCentered(text: String) {
        val textWidth = ImGui.calcTextSize(text).x

        ImGui.setCursorPosX(ImGui.getContentRegionMaxX() / 2 - (textWidth / 2))
        ImGui.text(text)
    }
}