package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.SeekBar
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.Models.RoundState
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_first_choice.*

class FirstChoiceActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.NEW.convertInt)

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
        textNumber2.text = (round.maxNumber/2).toString()
        seekBar2.progress = round.maxNumber/2

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
            round.opponentNumber = textNumber2.text.toString().toInt()
            val intent = Intent(this, SecondChoiceActivity::class.java)
            intent.putParcelableArrayListExtra(
                HomeActivity.CHALLENGES,
                challengesList as java.util.ArrayList<out Parcelable>
            )
            intent.putParcelableArrayListExtra(
                HomeActivity.PLAYERS,
                playersList as java.util.ArrayList<out Parcelable>
            )
            intent.putExtra(
                HomeActivity.ROUND,
                round)
            startActivity(intent)
        }

        //button home
        homebutton3.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
