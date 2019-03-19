package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_not_match.*

class NotMatchActivity : AppCompatActivity(), View.OnClickListener {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round()
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_not_match)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        // changement d'état du round
        if(round.number != RoundState.ONWAITING.convertInt)
            round.number++

        // gestion du bouton suivant
        buttonNextManager()
        soundManager.playSoundInLoop(SoundManager.RESULT)
        // affichage d'un texte explicite concernant la suite des évènements
        notMatchOrder.text = when(round.number) {
            RoundState.ONGOING.convertInt ->
            {
                StringBuilder().append(round.challenger?.username).append(", venge-toi !")
            }
            RoundState.ONFAILURE.convertInt ->
            {
                if(areThereAnyChallenges())
                    "Il n'y aura pas de match pour ce défi... Dommage !"
                else
                    "Pas de match pour ce défi... Et il ne semble plus y en avoir en stock :("
            }
            else -> StringBuilder().append(round.challenger?.username).append("clique sur l'écran")
        }

        // soundtrack lié à un game over
        soundManager.playSound(SoundManager.GAMEOVER)

        next.setOnClickListener(this)
        buttonNext.setOnClickListener(this)
        //button home
        homebutton7.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            buttonNext, next -> {
                // changement des infos liées à la variable ROUND
                val nextActivity = if(round.number == RoundState.ONGOING.convertInt) {
                    round.nextRound(ArrayList(), ArrayList())
                    FirstChoiceActivity::class.java
                } else{
                    // failure
                    if(areThereAnyChallenges() || round.number == RoundState.ONWAITING.convertInt) {
                        if(round.number != RoundState.ONWAITING.convertInt){
                            round.number = RoundState.ONNEW.convertInt
                            challengesList = round.nextRound(playersList!!, challengesList!!)
                        }
                        round.number = RoundState.ONWAITING.convertInt
                        VersusActivity::class.java
                    }
                    else
                        EndGameActivity::class.java
                }
                startActivity(
                    ActivityManager.switchActivity(this, nextActivity,
                    challengesList!!, playersList!!, round))
            }
            homebutton7 ->
            {
                startActivity(ActivityManager.backHome(this))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Trop tard... Les jeux sont faits !", Toast.LENGTH_SHORT).show()
    }

    private fun buttonNextManager(){
        buttonNext.text = if(round.number == RoundState.ONGOING.convertInt)
            "Vengeance"
        else {
            if(areThereAnyChallenges() || round.number == RoundState.ONWAITING.convertInt)
                "Prochain défi"
            else
                "Fin de partie"
        }
    }

    private fun areThereAnyChallenges() = challengesList?.size!! > 0

    override fun onPause() {
        super.onPause()
        soundManager.pauseAllSounds()
    }

    override fun onResume() {
        super.onResume()
        soundManager.restartAllSounds()
    }
}