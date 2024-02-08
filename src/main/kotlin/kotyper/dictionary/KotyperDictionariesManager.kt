package kotyper.dictionary

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

object KotyperDictionariesManager {
    private var dictionaries: HashMap<String, Dictionary> = HashMap()

    var currentDictionary: Dictionary? = null

    private val resources: List<String> = listOf("/english_5k.json", "/english.json", "/git.json", "/java.json")

    val availableDictionariesNames: MutableSet<String>
        get() = Collections.unmodifiableSet(dictionaries.keys);
    
    fun loadDictionaries() {
        val objectMapper = ObjectMapper()

        for (resourceName in resources) {
            val parsedDictionary = objectMapper.readValue<Dictionary>(javaClass.getResourceAsStream(resourceName)!!)
            dictionaries[resourceName.trim('/').dropLast(5)] = parsedDictionary
        }

        if (dictionaries.isNotEmpty())
            currentDictionary = dictionaries.values.first()
        else
            throw Exception("No available dictionaries")
    }

    fun setCurrentDictionary(dictionaryName: String) {
        currentDictionary = dictionaries[dictionaryName]!!
    }
}