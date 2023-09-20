package kotyper.dictionary

import com.beust.klaxon.Klaxon
import java.util.*

object DictionaryLoader {

    var dictionaries: HashMap<String, Dictionary> = HashMap()
        private set

    private val resources: List<String> = listOf("/english_5k.json", "/english.json", "/git.json")

    init {
        val jsonParser = Klaxon()

        for (resourceName in resources) {

            val parsedDictionary = jsonParser.parse<Dictionary>(this.javaClass.getResourceAsStream(resourceName)!!)

            dictionaries.put(resourceName.trim('/').dropLast(5), parsedDictionary!!)

        }
    }

    fun availableDictionariesNames(): MutableSet<String> {
        return Collections.unmodifiableSet(dictionaries.keys);
    }

}