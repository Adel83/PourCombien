package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_rules.*

class RulesActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        soundManager.playSoundInLoop(SoundManager.RULES)

        //button home
        homebutton10.setOnClickListener {
            soundManager.releaseAllSounds()
            startActivity(ActivityManager.backHome(this))
            finish()
        }
    }

    override fun onPause() {
        soundManager.pauseAllSounds()
        super.onPause()
    }

    override fun onResume() {
        soundManager.restartAllSounds()
        super.onResume()
    }

    override fun onBackPressed() {
        soundManager.releaseAllSounds()
        super.onBackPressed()
    }
}