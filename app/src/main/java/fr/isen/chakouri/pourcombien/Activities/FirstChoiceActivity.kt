package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_first_choice.*

class FirstChoiceActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round()
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_choice)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        val maxbar = round.maxNumber
        seekBar2!!.max = (maxbar - 1)

        playerTargeted.text = round.opponent?.username
        textNumber2.text = (round.maxNumber/2+1).toString()
        seekBar2.progress = round.maxNumber/2
        soundManager.playSoundInLoop(SoundManager.PLAYER_CHOICE)

        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val seekValue = progress + 1
               textNumber2.text = "$seekValue"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //button play
        buttonPlay3.setOnClickListener {
            soundManager.releaseAllSounds()
            round.opponentNumber = textNumber2.text.toString().toInt()
            startActivity(
                ActivityManager.switchActivity(this, SecondChoiceActivity::class.java,
                challengesList!!, playersList!!, round))
        }

        //button home
        homebutton3.setOnClickListener {
            soundManager.releaseAllSounds()
            startActivity(ActivityManager.backHome(this))
            finish()
        }
    }

    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Trop tard pour faire marche arrière... Assume, et joue !", Toast.LENGTH_LONG).show()
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