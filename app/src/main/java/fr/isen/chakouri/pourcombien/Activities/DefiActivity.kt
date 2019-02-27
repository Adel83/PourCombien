package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_defi.*

class DefiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_defi)

        //button play
        buttonPlay5.setOnClickListener {
            val intent = Intent(this, VersusActivity::class.java)
            startActivity(intent)
        }
    }
}
