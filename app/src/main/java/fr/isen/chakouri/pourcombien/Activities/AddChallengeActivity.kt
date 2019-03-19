package fr.isen.chakouri.pourcombien.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_add_challenge.*
import java.io.IOException
import java.util.*

class AddChallengeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val  PICK_IMAGE_REQUEST = 1234
        //Permission
        const val STORAGE_PERMISSION_CODE = 1
    }

    private var levelChosen: Level? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference


    // pour stockage de l'image
    private var filePath: Uri? = null
    private var storage: FirebaseStorage?=null
    private var storageReference: StorageReference?=null
    private var numberOfRequests: Int = 0
    private var soundManager = SoundManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_challenge)

        //button home
        homebutton12.setOnClickListener {
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        if(ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission()

        pictureChosen.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Toast.makeText(
                    //this, "You have already granted this permission!",
                   // Toast.LENGTH_SHORT
                //).show()
                showFileChooser()
            } else {
                requestStoragePermission()
            }
        }

        // initialisation de la base de stockage
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        // initialisation de la base de données en temps réel
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("challenge")

        buttonCreateChallenge.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v == pictureChosen)
            showFileChooser()
        else if (v == buttonCreateChallenge) {
            if(fieldsValidation())
            {
                val url: String? = uploadFile()
                val idRef = myRef.push().key.toString()
                myRef.child(idRef).setValue(Challenge(0, challengeQuestion.text.toString(), challengeOrder.text.toString(), idRef, levelChosen?.convertInt, url))
            }
            else
                Toast.makeText(this, "Merci de remplir tous les champs", Toast.LENGTH_LONG).show()
        }
    }

    @Suppress("DEPRECATION")
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
                    startActivity(ActivityManager.backHome(this))
                    finish()
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

    private fun fieldsValidation() =
        levelChosen != null && challengeQuestion.text.isNotEmpty() && challengeQuestion.text.isNotBlank()
                && challengeOrder.text.isNotEmpty() && challengeOrder.text.isNotBlank() && filePath != null

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
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val widthScale: Float =
                    if(bitmap.width < displayMetrics.widthPixels) 1f
                    else displayMetrics.widthPixels.div(bitmap.width.toFloat())
                val bitmapResized = Bitmap.createScaledBitmap(bitmap, (bitmap.width * widthScale).toInt(), (bitmap.height * widthScale).toInt(),true)
                pictureChosen!!.setImageBitmap(bitmapResized)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    //PERMISSION
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            AlertDialog.Builder(this)
                .setTitle("Permission requise")
                .setMessage("Afin de soumettre l'image de votre défi, il est nécessaire d'autoriser l'application à accéder à votre galerie.")
                .setPositiveButton("Suivant") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show()
                }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(numberOfRequests != 0)
                    showFileChooser()
            } else {
                Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show()
            }
            numberOfRequests++
        }
    }

    override fun onRestart(){
        super.onRestart()
        numberOfRequests = 0
        if(ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission()
    }

    override fun onPause() {
        super.onPause()
        soundManager.releaseAllSounds()
    }

    override fun onResume() {
        super.onResume()
        soundManager.playSoundInLoop(SoundManager.ADD_CHALLENGE)
    }
}