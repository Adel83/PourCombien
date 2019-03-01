package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.RadioButton
import com.google.firebase.database.*
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_get_challenges.*
import java.util.ArrayList

class GetChallengesActivity : AppCompatActivity() {

    // envoi des données vers une autre activité
    companion object {
        const val CHALLENGES = "challenges"
        const val PLAYERS = "players"
    }

    // connexion firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var levelChosen: Level? = null
    private var challengesList: ArrayList<Challenge> = ArrayList()
    private var playersList: ArrayList<Player> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_challenges)
        // TODO : réinitialiser les Parcelable
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        // ajout d'un défi
        //myRef.child(myRef.push().key.toString()).setValue(Challenge(5, "maman", myRef.push().key, "Bellydech"))
        //myRef.child(myRef.push().key.toString()).setValue(Category("hard"))
        /*myRef.child(Level.SILLY.convertInt.toString()).setValue(Category("debile", Level.SILLY.convertInt))
        myRef.child(Level.HARD.convertInt.toString()).setValue(Category("hard",Level.HARD.convertInt))
        myRef.child(Level.ALCOHOLIC.convertInt.toString()).setValue(Category("alcoolo", Level.ALCOHOLIC.convertInt))*/

        buttonGetChallenges.setOnClickListener {
            if(levelChosen != null) {
                loadChallenges()
            }
        }

        buttonNextStep.setOnClickListener {
            // simulation des différents joueurs
            playersList.add(Player(0, "Adel"))
            playersList.add(Player(1, "Théo"))
            playersList.add(Player(2, "Julien"))
            playersList.add(Player(3, "Julien"))
            val intent = Intent(this, NextStepActivity::class.java)
            intent.putParcelableArrayListExtra(CHALLENGES, challengesList as java.util.ArrayList<out Parcelable>)
            intent.putParcelableArrayListExtra(PLAYERS, playersList as java.util.ArrayList<out Parcelable>)
            startActivity(intent)
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

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
            }
        }

        myRef.addListenerForSingleValueEvent(messageListener)
    }

    fun onRadioButtonClicked(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked

            when(view.id){
                rButtonSilly.id ->
                    if(checked){
                        levelChosen = Level.SILLY
                    }
                rButtonHard.id ->
                    if(checked) {
                        levelChosen = Level.HARD
                    }
                rButtonAlcoholo.id ->
                    if(checked) {
                        levelChosen = Level.ALCOHOLIC
                    }
            }
        }
    }
}
