package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.ChallengeManager
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.R

class NextStepActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = ArrayList()
    private var playersList: java.util.ArrayList<Player>? = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_step)

        // récupération des challenges
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(GetChallengesActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(GetChallengesActivity.PLAYERS)
        }

        Toast.makeText(this,"Hello World", Toast.LENGTH_LONG).show()
        val challengeManager = ChallengeManager(challengesList)
        challengeManager.getRandomChallenge()
        val testRound = Round(0,null,null,0,0,0)
        testRound.maxNumber = 4
    }
}
