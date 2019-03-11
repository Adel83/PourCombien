package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_rules.*

class RulesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        //button home
        homebutton10.setOnClickListener {
            startActivity(ActivityManager.backHome(this))
            finish()
        }
    }
}
