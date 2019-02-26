package fr.isen.chakouri.pourcombien.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.reflect.Member

class SignUpActivity : AppCompatActivity() {

    private val REQUEST_CODE = 11
    private val JSON_FILE = "json_file"

    private lateinit var mAuth: FirebaseAuth

    // variables pour connexions à la base de données
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        // initialisation de la connexion à la bd
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("members")

        signinSubmit.setOnClickListener{
            writeUser()
        }

        picSignin.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }
    fun writeUser(){
        val name = textNom.text.toString()
        val firstName = textPrenom.text.toString()
        val nickname = textPseudo.text.toString()
        val password = textPsw.text.toString()
        val email = textMail.text.toString()

        signIn(name, firstName, nickname, password, email)

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

    private fun signIn(name: String, firstname: String, nickname: String, password: String, email: String) {
        if (!validateForm(name,firstname,nickname,password,email)) {
            return
        }
        mAuth.createUserWithEmailAndPassword(email, password)
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

    private fun validateForm(name: String,firstname: String,nickname: String,password: String,email: String): Boolean {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, "Enter a Name !", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(applicationContext, "Enter a Firstname !", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(applicationContext, "Enter a Nickname", Toast.LENGTH_SHORT).show()
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
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val uri = it.data
                if (uri != null) {
                    val stream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    picSignin.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // utilisateur connecté
            // redirection vers Home
            val intent = Intent(this, UploadImageActivity::class.java)
            startActivity(intent)
        } else {
            // utilisateur non connecté
            Toast.makeText(applicationContext, "Compte bizarre!", Toast.LENGTH_SHORT).show()
        }
    }
}
