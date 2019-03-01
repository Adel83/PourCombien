package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_not_match.*

class NotMatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_not_match)


        next.setOnClickListener {
            val intent = Intent(this, FirstChoiceActivity::class.java)
            startActivity(intent)
        }

        //button home
        homebutton7.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
