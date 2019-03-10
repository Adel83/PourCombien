package fr.isen.chakouri.pourcombien.Managers

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.support.annotation.RequiresApi
import fr.isen.chakouri.pourcombien.Models.Round
import java.util.*

class TTCManager(val context: Context, var textToSpeech: TextToSpeech? = null, private var readyTTC: Boolean = false){

    companion object {
        const val OPPONENT: String = "opponent"
        const val CHALLENGER: String = "challenger"
        const val PROVOCATION: String = "provocation"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun initTTC(type: String, round: Round){
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener {
            status: Int ->
                if(status == TextToSpeech.SUCCESS){
                    val voice = Voice("fr-fr-x-vlf#male_1-local", Locale.FRANCE, Voice.QUALITY_VERY_HIGH, Voice.LATENCY_VERY_HIGH, false, mutableSetOf())
                    textToSpeech?.voice = voice
                    textToSpeech?.setSpeechRate(0.75f)
                    readyTTC = true
                    makeASpeech(type, round)
                }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun makeASpeech(type: String, round: Round){
        if(readyTTC) {
            when(type) {
                CHALLENGER -> textToSpeech?.speak(round.challenger?.username, TextToSpeech.QUEUE_FLUSH, null, type)
                OPPONENT -> textToSpeech?.speak(round.opponent?.username, TextToSpeech.QUEUE_FLUSH, null, type)
                PROVOCATION ->
                {
                    val text: String = round.challenger?.username + "est défié par... " + round.opponent?.username
                    textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, type)
                }
            }
        }
    }
}