package kotyper.utils

import java.io.InputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

class AudioPlayer(private val clip: Clip = AudioSystem.getClip()) : Clip by clip {

    private var audioInputStream: AudioInputStream? = null

    constructor(inputStream: InputStream) : this() {
        println("new constructor еб твою мать блять как так то нахуй")
        this.audioInputStream = AudioSystem.getAudioInputStream(inputStream)
        clip.open(audioInputStream)
    }

    constructor(resourceName: String) : this() {
        println("2 new constructor еб твою мать блять как так то нахуй")
        audioInputStream = AudioSystem.getAudioInputStream(this.javaClass.getResourceAsStream(resourceName))

        clip.open(audioInputStream)

    }

    fun restart() {
        clip.framePosition = 0
        clip.start()
    }
}