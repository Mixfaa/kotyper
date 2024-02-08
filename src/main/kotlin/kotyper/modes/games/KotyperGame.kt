package kotyper.modes.games

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.readValue
import kotyper.modes.KotyperGameResult
import java.nio.file.Files
import java.nio.file.Paths

abstract class KotyperGame<GameResultType : KotyperGameResult>() {

    var name: String = "Some game"
        private set

    constructor(name: String) : this() {
        this.name = name
    }

    abstract fun drawGame()
    abstract fun drawPreGameGui()
    abstract fun charCallback(window: Long, c: Int)
    abstract fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mode: Int)
    abstract fun drawResults();
    abstract fun drawAfterGameWindow();

    abstract fun onGameStop()


    fun loadResults() {

        try {
            val parsedResults =
                objectMapper.readValue<List<GameResultType>>(Files.newInputStream(Paths.get("results/${this.name}.json")))

            this.results.addAll(parsedResults)
        } catch (e: Exception) {
            //e.printStackTrace()
        }
    }

    fun saveResults() {

        Files.createDirectories(Paths.get("results/"))

        val jsonResults = objectMapper.writeValueAsString(results)

        Files.newOutputStream(Paths.get("results/${this.name}.json")).write(jsonResults.toString().toByteArray())
    }

    val results: MutableList<GameResultType> = ArrayList();

    companion object {
        val objectMapper = ObjectMapper()

        init {
            objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfSubType("java.util.ArrayList")
                    .allowIfSubType(KotyperGameResult::class.java)
                    .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
            )
        }
    }
}