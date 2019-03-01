package fr.isen.chakouri.pourcombien.Activities

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.SeekBar
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Activities.Shake.OnShakeListener
import fr.isen.chakouri.pourcombien.Activities.Shake.ShakeDetector
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round
import fr.isen.chakouri.pourcombien.Models.RoundState
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {
    var numberLimit = "8"

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.NEW.convertInt)
    private lateinit var mSensorManager: SensorManager
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        challengerId.text = round.challenger?.username
        questionText.text = round.challenge?.content

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val seekValue0 = progress + 1
                textNumber1.text = "$seekValue0"
                numberLimit = "$seekValue0"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        // ShakeDetector initialization
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector(object: OnShakeListener {
            override fun onShake(count: Int) {
                Toast.makeText(applicationContext,"shaked",Toast.LENGTH_SHORT).show()
            }
        })

        //button play
        buttonPlay2.setOnClickListener {
            // enregistrement du nombre saisi
            round.maxNumber = textNumber1.text.toString().toInt()
            val intent = Intent(this, FirstChoiceActivity::class.java)
          
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

        //button home
        homebutton2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    public override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector)
        super.onPause()
    }
}
