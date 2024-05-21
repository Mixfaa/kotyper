package kotyper


import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import kotyper.dictionary.KotyperDictionariesManager
import kotyper.modes.KotyperGamesManager
import kotyper.utils.AudioPlayer

object KotyperGameManager {
    var currentState = Gamestate.IDLE
    var errorSound = AudioPlayer("/error.wav")

    fun charCallback(window: Long, c: Int) {
        if (currentState == Gamestate.PLAYING)
            KotyperGamesManager.currentGame.charCallback(window, c)
    }

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int) {
        if (currentState == Gamestate.PLAYING)
            KotyperGamesManager.currentGame.keyCallback(window, key, scancode, action, mode)
    }

    fun drawGui() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(KotyperWindow.WIDTH.toFloat(), KotyperWindow.HEIGHT.toFloat())

        ImGui.begin(
            "Kotyper main window",
            ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.NoResize
        )

        if (currentState == Gamestate.IDLE) {

            if (ImGui.treeNode("Some settings")) {
                if (ImGui.beginTabBar("Settings")) {
                    if (ImGui.beginTabItem("Choose dictionary")) {
                        KotyperDictionariesManager.availableDictionariesNames.forEach { dictionaryName ->

                            if (ImGui.selectable(
                                    dictionaryName,
                                    KotyperDictionariesManager.currentDictionary?.name == dictionaryName
                                )
                            )
                                KotyperDictionariesManager.setCurrentDictionary(dictionaryName)
                        }
                        ImGui.endTabItem()
                    }
                    if (ImGui.beginTabItem("Choose game")) {
                        KotyperGamesManager.gamesList.forEach { game ->
                            if (ImGui.selectable(
                                    game::class.simpleName,
                                    KotyperGamesManager.currentGame == game
                                )
                            ) // game.name
                                KotyperGamesManager.currentGame = game
                        }

                        ImGui.endTabItem()
                    }

                    ImGui.endTabBar()
                }

                ImGui.treePop()
                ImGui.separator()
            }

            if (ImGui.treeNode("Game results")) {
                KotyperGamesManager.currentGame.drawResults()

                ImGui.treePop()
                ImGui.separator()
            }

            KotyperGamesManager.currentGame.drawPreGameGui()
        }

        if (currentState == Gamestate.PLAYING) {
            KotyperGamesManager.currentGame.drawGame()

            if (ImGui.isKeyReleased(256)) {
                KotyperGamesManager.currentGame.onGameStop()
                currentState = Gamestate.IDLE
            }

        }

        if (currentState == Gamestate.AFTER_GAME) {
            KotyperGamesManager.currentGame.drawAfterGameWindow()
        }

        ImGui.end()
    }

    fun onStartup() {
        KotyperDictionariesManager.loadDictionaries()
        KotyperGamesManager.loadResults()

    }

    fun onClose() {
        KotyperGamesManager.saveResults()
    }

}