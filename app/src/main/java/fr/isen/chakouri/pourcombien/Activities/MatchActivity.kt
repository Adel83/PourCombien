package fr.isen.chakouri.pourcombien.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
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

        matchOrder.text = StringBuilder().append(round.challenger?.username).append(",\nassume et relève le défi")
        // changement d'état du round
        round.number = RoundState.ONSUCCESS.convertInt
        // soundtrack de match
        soundManager.playSound(SoundManager.MATCH)
        soundManager.playSoundInLoop(SoundManager.RESULT)
        // étape suivante (vers activité défi)
        next2.setOnClickListener(this)
        //button home
        homebutton6.setOnClickListener(this)

        // vibreur
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        @Suppress("DEPRECATION")
        vibrator.vibrate(longArrayOf(100,200,100,400,100,1000),-1)
    }

    override fun onClick(v: View){
        when(v){
            next2 -> {
                soundManager.releaseAllSounds()
                startActivity(
                    ActivityManager.switchActivity(
                        this, DefiActivity::class.java,
                        challengesList!!, playersList!!, round
                    )
                )
            }
            homebutton6 ->
            {
                soundManager.releaseAllSounds()
                startActivity(ActivityManager.backHome(this))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Trop tard... Les jeux sont faits !", Toast.LENGTH_SHORT).show()
    }

    override fun onPause(){
        soundManager.pauseAllSounds()
        super.onPause()
    }

    override fun onResume() {
        soundManager.restartAllSounds()
        super.onResume()
    }
}