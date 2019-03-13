package fr.isen.chakouri.pourcombien.Activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.chakouri.pourcombien.Activities.Shake.OnShakeListener
import fr.isen.chakouri.pourcombien.Activities.Shake.ShakeDetector
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
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
    private var myRef: DatabaseReference? = null
    private var challengeLiked: Boolean = false // booléan permettant de liker une seule fois l'activité par game
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // récupération des challenges et de la liste des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        if(round.challenge != null){
            myRef = FirebaseDatabase.getInstance().getReference("challenge/${round.challenge!!.id}/like")
        }

        challengerId.text = round.challenger?.username
        questionText.text = StringBuilder().append(round.challenge?.question).append(" ?").toString()
        likesField.text = round.challenge?.like.toString()

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
        // button like
        imageLike.setOnClickListener(this)
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
            imageLike ->
            {
                if(myRef != null) {
                    if(!challengeLiked) {
                        round.challenge?.updateNumberOfLikes(1, myRef, this)
                    }
                    else{
                        round.challenge?.updateNumberOfLikes(-1, myRef, this)
                    }
                    imageLike.isEnabled = false // désactivation du click en attendant le callback
                }
            }
        }
    }

    override fun onBackPressed() {
        // retour interdit
        Toast.makeText(this, "Le duel est : ${round.challenger?.username} VS ${round.opponent?.username}", Toast.LENGTH_LONG).show()
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
            questionText.text = StringBuilder().append(round.challenge?.question).append(" ?")
            // mise à jour du nombre likes
            likesField.text = round.challenge?.like.toString()
            // remise à false du booléan du vote
            challengeLiked = false
            myRef = FirebaseDatabase.getInstance().getReference("challenge/${round.challenge!!.id}/like")
            imageLike.setImageResource(R.drawable.like)
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
        soundManager.releaseAllSounds()
        super.onPause()
    }

    fun onLikeAction(state: Int){
        when(state){
            DatabaseState.UPDATE_LIKE_SUCCESS ->
            {
                likesField.text = (likesField.text.toString().toInt() + 1).toString()
                soundManager.playSound(SoundManager.LIKE)
                imageLike.setImageResource(R.drawable.likefull)
                challengeLiked = true
            }
            DatabaseState.UPDATE_UNLIKE_SUCCESS ->
            {
                likesField.text = (likesField.text.toString().toInt() - 1).toString()
                soundManager.playSound(SoundManager.UNLIKE)
                imageLike.setImageResource(R.drawable.like)
                challengeLiked = false
            }
        }
        // réactivation du click
        imageLike.isEnabled = true
    }

    override fun onStop(){
        soundManager.releaseAllSounds()
        super.onStop()
    }
}