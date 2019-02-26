package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.google.firebase.database.*
import fr.isen.chakouri.pourcombien.Models.Category
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_get_challenges.*
import java.util.ArrayList

class GetChallengesActivity : AppCompatActivity() {

    // connexion firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var levelChoosen: Level? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_challenges)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        // ajout d'un défi
        //myRef.child(myRef.push().key.toString()).setValue(Challenge(5, "maman", myRef.push().key, "Bellydech"))
        //myRef.child(myRef.push().key.toString()).setValue(Category("hard"))
        /*myRef.child(Level.SILLY.convertInt.toString()).setValue(Category("debile", Level.SILLY.convertInt))
        myRef.child(Level.HARD.convertInt.toString()).setValue(Category("hard",Level.HARD.convertInt))
        myRef.child(Level.ALCOHOLIC.convertInt.toString()).setValue(Category("alcoolo", Level.ALCOHOLIC.convertInt))*/

        buttonGetChallenges.setOnClickListener {
            val messageListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val postsList: ArrayList<Challenge?> = ArrayList()
                        for(ds in dataSnapshot.children)
                        {
                            postsList.add(ds.getValue(Challenge::class.java))
                        }

                        postsList.reverse()

                    }

                }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Failed to read value
                }
            }

            myRef.addListenerForSingleValueEvent(messageListener)

        }
    }

    fun onRadioButtonClicked(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked

            when(view.id){
                rButtonSilly.id ->
                    if(checked){
                        levelChoosen = Level.SILLY
                    }
                rButtonHard.id ->
                    if(checked) {
                        levelChoosen = Level.HARD
                    }
                rButtonAlcoholo.id ->
                    if(checked) {
                        levelChoosen = Level.ALCOHOLIC
                    }
            }
        }
    }
}
