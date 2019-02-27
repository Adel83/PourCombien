package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_mode.*

class ModeActivity : AppCompatActivity(), View.OnClickListener {

    private var levelChosen: Level? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        sillybutton.setOnClickListener(this)
        hard_button.setOnClickListener(this)
        alcoholobutton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            sillybutton -> levelChosen = Level.SILLY
            hard_button -> levelChosen = Level.HARD
            alcoholobutton -> levelChosen = Level.ALCOHOLIC
        }

        val intent = Intent(this, VersusActivity::class.java)
        startActivity(intent)
    }
}
