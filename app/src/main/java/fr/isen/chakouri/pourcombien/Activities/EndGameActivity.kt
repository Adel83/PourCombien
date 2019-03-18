package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        soundManager.playSoundInLoop(SoundManager.MAIN_THEME)

        //button home
        homebutton14.setOnClickListener {
            soundManager.releaseAllSounds()
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        //button lifecycle
        addchallenge4.setOnClickListener {
            soundManager.releaseAllSounds()
            val intent = Intent(this, AddChallengeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

    }
    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Trop tard... Les jeux sont faits !", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        soundManager.pauseAllSounds()
    }

    override fun onResume() {
        super.onResume()
        soundManager.restartAllSounds()
    }
}