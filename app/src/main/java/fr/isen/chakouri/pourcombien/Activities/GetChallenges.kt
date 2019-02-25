package fr.isen.chakouri.pourcombien.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.R

class GetChallenges : AppCompatActivity() {

    // connexion firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_challenges)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("category")

        myRef.child(myRef.push().key.toString()).setValue(Challenge(5, "maman", myRef.push().key, "Bellydech"))
    }
}
