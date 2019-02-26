package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_add_challenge.*

class AddChallengeActivity : AppCompatActivity() {

        private var levelChoosen: Level? = null
        private lateinit var database: FirebaseDatabase
        private lateinit var myRef: DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_challenge)

            database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        buttonCreateChallenge.setOnClickListener {
            if(levelChoosen != null && challengeText.text.isNotEmpty() && challengeText.text.isNotBlank())
            {
                myRef.child(myRef.push().key.toString()).setValue(Challenge(0, challengeText.text.toString(), myRef.push().key.toString(), levelChoosen?.convertInt))
            }
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
