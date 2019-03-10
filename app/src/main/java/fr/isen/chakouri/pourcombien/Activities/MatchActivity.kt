package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_match.*

class MatchActivity : AppCompatActivity(), View.OnClickListener {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.ONSUCCESS.convertInt)
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        namePlayerField.text = round.challenger?.username
        // changement d'état du round
        round.number = RoundState.ONSUCCESS.convertInt
        // soundtrack de match
        soundManager.playSound(SoundManager.MATCH)

        // étape suivante (vers activité défi)
        next2.setOnClickListener(this)
        //button home
        homebutton6.setOnClickListener(this)
    }

    override fun onClick(v: View){
        when(v){
            next2 ->
                startActivity(
                    ActivityManager.switchActivity(this, DefiActivity::class.java,
                    challengesList!!, playersList!!, round))
            homebutton6 ->
            {
                startActivity(ActivityManager.backHome(this))
                finish()
            }
        }
    }
}