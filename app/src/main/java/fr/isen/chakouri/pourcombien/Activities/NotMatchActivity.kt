package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        round.number++

        // gestion du bouton suivant
        buttonNextManager()

        namePlayerField.text = round.challenger?.username
        // soundtrack lié à un game over
        soundManager.playSound(SoundManager.GAMEOVER)

        next.setOnClickListener(this)
        //button home
        homebutton7.setOnClickListener(this)

        // TODO : à voir si on garde le bouton "Suivant"
    }

    override fun onClick(v: View?) {
        when(v){
            next -> {
                // changement des infos liées à la variable ROUND
                val nextActivity = if(round.number == RoundState.ONGOING.convertInt) {
                    round.nextRound(ArrayList(), ArrayList())
                    FirstChoiceActivity::class.java
                } else{
                    // failure
                    if(areThereAnyChallenges())
                        VersusActivity::class.java
                    else
                        HomeActivity::class.java
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

    fun buttonNextManager(){
        buttonNext.text = if(round.number == RoundState.ONGOING.convertInt)
            "Vengeance"
        else {
            if(areThereAnyChallenges())
                "Prochain défi"
            else
                "Fin de partie"
        }
    }

    fun areThereAnyChallenges() = challengesList?.size!! > 0
}
