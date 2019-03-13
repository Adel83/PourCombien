package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Managers.ChallengeManager
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_next_step.*

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
        val challengeSelected = challengeManager.getRandomChallenge()
        if(challengeSelected != null)
        {
            // affichage du texte
            challengeText.text = challengeSelected.question
            // affichage de l'image
            loadWithPicassoByUrl(challengeSelected.urlImage)

        }
        // round
        val testRound = Round(0,null,null,0,0,0)
        testRound.maxNumber = 4
    }

    private fun loadWithPicassoByUrl(url: String?) {
        if(url != null) {
            val rightUrl =
                "https://firebasestorage.googleapis.com/v0/b/pour-combien-tu.appspot.com/o/images%2F$url?alt=media"
            Picasso
                .get()
                .load(rightUrl)
                .into(challengeImage)
        }
    }
}
