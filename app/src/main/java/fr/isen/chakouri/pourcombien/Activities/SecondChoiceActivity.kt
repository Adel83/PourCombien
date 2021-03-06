package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_second_choice.*

class SecondChoiceActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round()
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_choice)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        val maxbar = round.maxNumber
        seekBar3!!.max = (maxbar - 1)

        playerTargeted.text = round.challenger?.username
        textNumber3.text = (round.maxNumber/2+1).toString()
        seekBar3.progress = round.maxNumber/2
        soundManager.playSoundInLoop(SoundManager.PLAYER_CHOICE)

        seekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val seekValue2 = progress + 1
                textNumber3.text = "$seekValue2"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        //button home
        homebutton4.setOnClickListener {
            soundManager.releaseAllSounds()
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        buttonPlay4.setOnClickListener {
            soundManager.releaseAllSounds()
            round.challengerNumber = textNumber3.text.toString().toInt()
            startActivity(
                ActivityManager.switchActivity(this, determineNextActivity(),
                challengesList!!, playersList!!, round))
        }
    }

    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Petit tricheur...", Toast.LENGTH_SHORT).show()
    }

    private fun determineNextActivity(): Class<*> {
        return if(round.challengerNumber == round.opponentNumber)
            MatchActivity::class.java
        else
            NotMatchActivity::class.java
    }

    override fun onPause(){
        soundManager.pauseAllSounds()
        super.onPause()
    }

    override fun onResume(){
        soundManager.restartAllSounds()
        super.onResume()
    }
}