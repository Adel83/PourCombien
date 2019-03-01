package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.SeekBar
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.Models.RoundState
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_second_choice.*

class SecondChoiceActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.NEW.convertInt)

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
        textNumber3.text = (round.maxNumber/2).toString()
        seekBar3.progress = round.maxNumber/2

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
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        buttonPlay4.setOnClickListener {
            round.challengerNumber = textNumber3.text.toString().toInt()
            val nextActivity = determineNextActivity()
            val intent = Intent(this, nextActivity)
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
    }

    private fun determineNextActivity(): Class<*> {
        return if(round.challengerNumber == round.opponentNumber)
            MatchActivity::class.java
        else
            NotMatchActivity::class.java
    }
}
