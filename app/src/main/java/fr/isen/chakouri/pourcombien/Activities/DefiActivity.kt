package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_defi.*
import java.lang.Exception

class DefiActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.ONSUCCESS.convertInt)
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_defi)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        challengeText.text = round.challenge?.order
        challengerId.text = round.challenger?.username
        loadImage("https://firebasestorage.googleapis.com/v0/b/pour-combien-tu.appspot.com/o/images%2F" + round.challenge?.urlImage + "?alt=media")

        //button play
        nextScreen.setOnClickListener {
            soundManager.releaseAllSounds()
            if (areThereAnyChallenges() || round.number == RoundState.ONWAITING.convertInt) {
                if(round.number != RoundState.ONWAITING.convertInt){
                    round.number = RoundState.ONNEW.convertInt
                    challengesList = round.nextRound(playersList!!, challengesList!!)
                }
                round.number = RoundState.ONWAITING.convertInt
                startActivity(ActivityManager.switchActivity(
                        this, VersusActivity::class.java,
                        challengesList!!, playersList!!, round
                    )
                )
            } else {
                startActivity(ActivityManager.goEnd(this))
                finish()
            }
        }

        //button home
        homebutton5.setOnClickListener {
            soundManager.releaseAllSounds()
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        /*
        // DEPRECATED :
        Handler().postDelayed({
            loadingPanel.visibility = View.GONE
        }, 1500)
        */
        soundManager.playSound(SoundManager.THRILLER_LAUGH)
        soundManager.playSoundInLoop(SoundManager.RESULT)
    }

    private fun areThereAnyChallenges() = challengesList?.size!! > 0

    private fun loadImage(url: String?) {
        if(url != null) {
            Picasso
                .get()
                .load(url)
                .into(imageChallenge,
                object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        loadingPanel.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                    }
                })
        }
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