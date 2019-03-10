package fr.isen.chakouri.pourcombien.Activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import fr.isen.chakouri.pourcombien.Activities.Shake.OnShakeListener
import fr.isen.chakouri.pourcombien.Activities.Shake.ShakeDetector
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity(), View.OnClickListener {
    var numberLimit = "8"

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.ONNEW.convertInt)
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
                changeChallenge()
            }
        })

        // image reload
        reloadImage.setOnClickListener(this)
        //button play
        buttonPlay2.setOnClickListener(this)
        //button home
        homebutton2.setOnClickListener(this)
    }

    override fun onClick(v: View){
        when(v){
            reloadImage -> changeChallenge()
            buttonPlay2 -> beginChallenge()
            homebutton2 ->
            {
                startActivity(ActivityManager.backHome(this))
                finish()
            }
        }
    }

    private fun beginChallenge() {
        // enregistrement du nombre saisi
        round.maxNumber = textNumber1.text.toString().toInt()
        startActivity(
            ActivityManager.switchActivity(this, FirstChoiceActivity::class.java,
            challengesList!!, playersList!!, round))
    }

    private fun changeChallenge() {
        if (challengesList != null && challengesList!!.size > 0) {
            challengesList = round.getChallenge(challengesList!!)
            // mise à jour du défi
            questionText.text = round.challenge?.content
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
