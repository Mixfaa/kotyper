package kotyper.utils

import imgui.ImGui
import imgui.flag.ImGuiWindowFlags

object KotyperImGui {


    private fun displayUnderline() {
        val prevCursorPos = ImGui.getCursorPos()

        ImGui.setCursorPosY(prevCursorPos.y + 5f)

        ImGui.textDisabled("_")

        ImGui.setCursorPosX(prevCursorPos.x)
        ImGui.setCursorPosY(prevCursorPos.y)
    }

    fun displayTextToType(textToType: String, typedText: String) {

        ImGui.setNextWindowSize(600f, 100f)

        ImGui.begin("###wndw", ImGuiWindowFlags.AlwaysAutoResize or ImGuiWindowFlags.NoDecoration)


        for (index in textToType.indices) {

            val symbol = textToType[index]

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

        ImGui.end()

    }
}