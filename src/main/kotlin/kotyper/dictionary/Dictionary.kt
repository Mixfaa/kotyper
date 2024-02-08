package kotyper.dictionary

import kotlin.random.Random

class Dictionary {
    val name: String = "default dictionary name";
    val words: MutableList<String> = ArrayList()

    fun getRandomText(count: Int): String {
        val resultBuilder = StringBuilder()

        var prevWordIndex: Int = -1
        for (i in 0..<count) {
            var randomIndex = Random.nextInt(words.size)

            while (prevWordIndex == randomIndex)
                randomIndex = Random.nextInt(words.size)

            prevWordIndex = randomIndex

            resultBuilder.append(words[randomIndex])
            resultBuilder.append(' ')
        }

        return resultBuilder.trimEnd().toString()

    }

}