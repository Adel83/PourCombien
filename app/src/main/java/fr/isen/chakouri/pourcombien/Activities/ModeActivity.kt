package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_mode.*
import java.util.ArrayList

class ModeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myRef: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private var levelChosen: Level? = null
    private var challengesList: ArrayList<Challenge> = ArrayList()
    private var playersList: ArrayList<Player> = ArrayList()

    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        // chargement des joueurs
        intent?.let { playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)}

        // boutons représentants les modes de jeu
        sillybutton.setOnClickListener(this)
        hard_button.setOnClickListener(this)
        alcoholobutton.setOnClickListener(this)
        //button home
        homebutton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            sillybutton -> levelChosen = Level.SILLY
            hard_button -> levelChosen = Level.HARD
            alcoholobutton -> levelChosen = Level.ALCOHOLIC
            homebutton -> {
                startActivity(ActivityManager.backHome(this))
                finish()
            }
        }

        if(levelChosen != null) {
            loadChallenges()
        }
    }

    private fun loadChallenges() {
        val messageListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //val challengesList: ArrayList<Challenge?> = ArrayList()
                    for (ds in dataSnapshot.children) {
                        val challenge = ds.getValue(Challenge::class.java)
                        if (challenge?.idCategory == levelChosen?.convertInt) {
                            challenge?.let { challengesList.add(it) }
                        }
                    }
                }
                if(challengesList.size != 0) {
                    val round = Round(RoundState.ONNEW.convertInt)
                    /*val intent = Intent(applicationContext, VersusActivity::class.java)
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
                    startActivity(intent)*/
                    startActivity(ActivityManager.switchActivity(applicationContext, VersusActivity::class.java, challengesList, playersList, round))
                }
                else {
                    Toast.makeText(applicationContext, "Aucun challenge trouvé", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Toast.makeText(applicationContext, "Erreur de chargement", Toast.LENGTH_SHORT).show()
            }
        }

        myRef.addListenerForSingleValueEvent(messageListener)
    }

    override fun onPause() {
        super.onPause()
        soundManager.releaseAllSounds()
    }

    override fun onResume() {
        super.onResume()
        challengesList = ArrayList()
        soundManager.playSoundInLoop(SoundManager.MODE)
    }
}
