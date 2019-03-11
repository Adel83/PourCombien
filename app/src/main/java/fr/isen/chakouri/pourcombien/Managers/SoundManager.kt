package fr.isen.chakouri.pourcombien.Managers

import android.content.Context
import android.media.MediaPlayer
import fr.isen.chakouri.pourcombien.R

class SoundManager(val context: Context, var mediaPlayer: MediaPlayer? = null){
    companion object {
        const val GAMEOVER = R.raw.game_over
        const val MATCH = R.raw.match
        const val LIKE = R.raw.like
        const val UNLIKE = R.raw.unlike
    }

    fun playSound(soundId: Int){
        mediaPlayer = MediaPlayer.create(context, soundId)
        if(mediaPlayer != null){
            // si l'initialisation s'est bien passée
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                // fin du son
                stopSoundStrack()
            }
        }
    }

    fun stopSoundStrack(){
        if(mediaPlayer != null){
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}