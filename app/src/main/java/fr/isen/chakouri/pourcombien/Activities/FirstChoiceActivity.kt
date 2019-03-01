package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_first_choice.*

class FirstChoiceActivity : AppCompatActivity() {

    var numberLimit2 = "8"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_choice)

        var ss:String = intent.getStringExtra("max")
        val maxbar = ss.toInt()
        seekBar2!!.max = (maxbar - 1)






        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val seekValue = progress + 1
               textNumber2.text = "$seekValue"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //button play
        buttonPlay3.setOnClickListener {
            val intent = Intent(this, SecondChoiceActivity::class.java)
            intent.putExtra("max2", ss)
            startActivity(intent)
        }

        //button home
        homebutton3.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}

private fun SeekBar.max(i: Int) {

}
