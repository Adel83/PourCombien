package fr.isen.chakouri.pourcombien.Managers

import android.content.Context
import android.media.MediaPlayer
import fr.isen.chakouri.pourcombien.R

class SoundManager(val context: Context, var mediaPlayersList: ArrayList<MediaPlayer> = ArrayList()){
    companion object {
        const val GAMEOVER = R.raw.game_over
        const val MATCH = R.raw.match
        const val LIKE = R.raw.like
        const val UNLIKE = R.raw.unlike
    }

    fun playSound(soundId: Int){
        val mediaPlayer = MediaPlayer.create(context, soundId)
        if(mediaPlayer != null){
            // création du MediaPlayer avec succès
            mediaPlayersList.add(mediaPlayer)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                // fin du son
                releaseSound(mediaPlayer)
            }
        }

    }

    private fun releaseSound(mediaPlayer: MediaPlayer){
        // suppression de la liste
        mediaPlayersList.remove(mediaPlayer)
        mediaPlayer.release()
    }

    fun releaseAllSounds(){
        for(mediaPlayer in mediaPlayersList)
            mediaPlayer.release()
        mediaPlayersList.clear()
    }
}