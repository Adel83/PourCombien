package fr.isen.chakouri.pourcombien.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Level
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_add_challenge.*
import java.io.IOException
import java.util.*

class AddChallengeActivity : AppCompatActivity(), View.OnClickListener {

    private val  PICK_IMAGE_REQUEST = 1234

    //Permission
    private val STORAGE_PERMISSION_CODE = 1

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

        //button home
        homebutton12.setOnClickListener {
            startActivity(ActivityManager.backHome(this))
            finish()
        }

        val buttonRequest = findViewById<View>(R.id.pictureChosen)
        buttonRequest.setOnClickListener {
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

        buttonCreateChallenge.setOnClickListener {
            if(fieldsValiditation())
            {
                myRef.child(myRef.push().key.toString()).setValue(Challenge(0, challengeText.text.toString(), myRef.push().key.toString(), levelChosen?.convertInt))
            }
        }
        buttonCreateChallenge.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v == pictureChosen)
            showFileChooser()
        else if (v == buttonCreateChallenge) {
            if(fieldsValiditation())
            {
                val url: String? = uploadFile()
                val idRef = myRef.push().key.toString()
                myRef.child(idRef).setValue(Challenge(0, challengeText.text.toString(), idRef, levelChosen?.convertInt, url))
                startActivity(ActivityManager.backHome(this))
                finish()
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
    //PERMISSION
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of the safety of our user's personal data")
                .setPositiveButton("ok") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
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
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
