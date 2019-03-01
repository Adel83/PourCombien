package fr.isen.chakouri.pourcombien.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_add_challenge.*
import java.io.IOException
import java.util.*

class AddChallengeActivity : AppCompatActivity(), View.OnClickListener {

    private val  PICK_IMAGE_REQUEST = 1234

    private var levelChosen: Level? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference


    // pour stockage de l'image
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_challenge)

        // initialisation de la base de stockage
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        // initialisation de la base de données en temps réel
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        buttonCreateChallenge.setOnClickListener {
            if(fieldsValiditation())
            {
                myRef.child(myRef.push().key.toString()).setValue(Challenge(0, challengeText.text.toString(), myRef.push().key.toString(), levelChosen?.convertInt))
            }
        }

        pictureChosen.setOnClickListener(this)
        buttonCreateChallenge.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v == pictureChosen)
            showFileChooser()
        else if (v == buttonCreateChallenge) {
            if(fieldsValiditation())
            {
                val url: String? = uploadFile()
                myRef.child(myRef.push().key.toString()).setValue(Challenge(0, challengeText.text.toString(), myRef.push().key.toString(), levelChosen?.convertInt, url))
            }
        }
    }

    private fun uploadFile(): String? {
        if (filePath != null){

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/"+ UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    progressDialog.dismiss()
                    Log.e("UploadImageActivity",it.message)
                    Toast.makeText(applicationContext,"Failed", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot->
                    val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                    progressDialog.setMessage("Uploaded "+progress.toInt() + "%...")
                }

                return imageRef.name
        }
        else return null
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST)

    }

    private fun fieldsValiditation() =
        levelChosen != null && challengeText.text.isNotEmpty() && challengeText.text.isNotBlank()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data != null )
        {
            filePath = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                pictureChosen!!.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
}
