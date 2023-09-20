package kotyper.dictionary

import kotlin.random.Random

class Dictionary(name: String) {
    private var name: String = "default dictionary name"
    private var words: MutableList<String> = ArrayList()

    init {
        this.name = name
    }

    fun getRandomWord(): String {
        val randomIndex = Random.nextInt(words.size)
        return words[randomIndex]
    }

    fun getRandomWords(count: Int): List<String> {
        val wordsList = ArrayList<String>()

        for (i in 0.rangeUntil(count)) {
            val randomIndex = Random.nextInt(words.size)
            wordsList.add(words[randomIndex])
        }

        return wordsList;
    }

    fun getRandomText(count: Int): String {
        val resultBuilder = StringBuilder()

        for (i in 0.rangeUntil(count)) {
            val randomIndex = Random.nextInt(words.size)

            resultBuilder.append(words[randomIndex])
            resultBuilder.append(' ')
        }

        return resultBuilder.trimEnd().toString()

    }

}