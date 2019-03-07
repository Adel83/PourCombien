package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.squareup.picasso.Picasso
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.Models.RoundState
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_defi.*

class DefiActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.NEW.convertInt)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_defi)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        challengeText.text = round.challenge?.content
        loadImage("https://firebasestorage.googleapis.com/v0/b/pour-combien-tu.appspot.com/o/images%2F" + round.challenge?.urlImage + "?alt=media")
        // gestion du bouton suivant
        buttonNextManager()

        //button play
        buttonNext.setOnClickListener {
            if(buttonNext.text.toString() == "Suivant"){
                val intent = Intent(this, VersusActivity::class.java)
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
            else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        //button home
        homebutton5.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }


    fun buttonNextManager(){
        buttonNext.text = if(areThereAnyChallenges()) "Suivant" else "Fin de partie"
    }

    fun areThereAnyChallenges() = challengesList?.size!! > 0

    fun loadImage(url: String?){
        if(url!=null){
            Picasso
                .get()
                .load(url)
                .into(imageChallenge)
        }
    }
}
