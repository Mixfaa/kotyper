package kotyper.modes

import kotyper.modes.games.DefaultGame
import kotyper.modes.games.TimeBasedGame

object KotyperGamesManager {
    val gamesList = listOf(DefaultGame(), TimeBasedGame())
    var currentGame = gamesList.first()

    fun loadResults() = gamesList.forEach { it.loadResults() }

    fun saveResults() = gamesList.forEach { it.saveResults() }

}