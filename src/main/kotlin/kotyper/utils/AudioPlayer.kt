package kotyper.utils

import java.io.BufferedInputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip


class AudioPlayer(resourceName: String, private val clip: Clip = AudioSystem.getClip()) : Clip by clip {
    private val audioInputStream: AudioInputStream

    init {
        val resourceStream = this.javaClass.getResourceAsStream(resourceName)
        val bufferedIn = BufferedInputStream(resourceStream!!)
        audioInputStream = AudioSystem.getAudioInputStream(bufferedIn)
        clip.open(audioInputStream)

    }

    fun restart() {
        clip.framePosition = 0
        clip.start()
    }
}