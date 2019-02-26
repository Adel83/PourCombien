package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    lateinit var pref: SharedPreferences
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        mAuth = FirebaseAuth.getInstance()
        submitButton.setOnClickListener {
            writeUser()
        }
    }

    fun writeUser(){
        val email = emailField.text.toString()
        val password = passwordFieldLogIn.text.toString()

        signIn(email,password)

        /*if(name.isNotEmpty() && firstName.isNotEmpty() && nickname.isNotEmpty() && password.isNotEmpty() && mail.isNotEmpty()){
            val jsonString = "{'name' : '$name', 'firstName' : '$firstName', 'nickname' : '$nickname', 'password' : '$password', 'mail' : '$mail'}"

            val fos: FileOutputStream
            fos = openFileOutput(JSON_FILE, Context.MODE_PRIVATE)
            fos.write(jsonString.toByteArray())
            fos.close()
            Toast.makeText(this, "Votre nom est $name et votre pseudo est $nickname", Toast.LENGTH_LONG).show()
            //Vider les fields
        }else{
            Toast.makeText(this, "Vous n'avez pas renseigner tout les champs", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun signIn(email: String,password: String) {
        if (!validateForm(email,password)) {
            return
        }
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // update UI with the signed-in user's information
                    val user = mAuth.getCurrentUser()
                    updateUI(user)
                } else {
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Compte inexistant!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // utilisateur connecté
            // redirection vers Home
            val intent = Intent(this, UploadImageActivity::class.java)
            startActivity(intent)
        } else {
            // utilisateur non connecté
            // Toast.makeText(applicationContext, "Compte inexistant", Toast.LENGTH_SHORT).show()
        }
    }
}
