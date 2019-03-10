package fr.isen.chakouri.pourcombien.Activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.TTCManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_versus.*
import java.util.*

class VersusActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.ONNEW.convertInt) // initialisation de base à ONNEW
    private lateinit var textToSpeech: TextToSpeech

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_versus)
        // TODO : ne pas autoriser le retour en arrière
        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        if(challengesList != null)
        {
            if(playersList != null)
            {
                challengesList = round.nextRound(playersList!!, challengesList!!)
                player1Field.text = round.challenger?.username
                player2Field.text = round.opponent?.username

                val ttcManager = TTCManager(this)
                ttcManager.initTTC(TTCManager.PROVOCATION, round)
            }
        }
        else{
            Toast.makeText(this, "Plus de challenges", Toast.LENGTH_SHORT).show()
        }

        Layout.setOnClickListener {
            startActivity(
                ActivityManager.switchActivity(this, QuestionActivity::class.java,
                challengesList!!, playersList!!, round))
        }
    }
}