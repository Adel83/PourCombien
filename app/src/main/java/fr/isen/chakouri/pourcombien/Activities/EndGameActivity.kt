package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        //button home
        homebutton14.setOnClickListener {
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        //button lifecycle
        addchallenge4.setOnClickListener {
            val intent = Intent(this, AddChallengeActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Trop tard... Les jeux sont faits !", Toast.LENGTH_SHORT).show()
    }
}