package fr.isen.chakouri.pourcombien.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_upload_image.*
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*


class UploadImageActivity : AppCompatActivity(), View.OnClickListener {

    private val  PICK_IMAGE_REQUEST = 1234

    override fun onClick(v: View) {
        if (v == pictureUpload)
            showFileChooser()
        else if (v === buttonUpload)
            uploadFile()

    }

    private fun uploadFile() {
        if (filePath != null){

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/"+ UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    progressDialog.dismiss()
                    Log.e("UploadImageActivity",it.message)
                    Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot->
                    val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                    progressDialog.setMessage("Uploaded "+progress.toInt() + "%...")
                }


        }

    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST)

    }

    private val REQUEST_CODE = 11
    private var filePath: Uri? = null

    internal var storage:FirebaseStorage?=null
    internal var storageReference:StorageReference?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        //Init firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        //Setup button
        pictureUpload.setOnClickListener(this)
        buttonUpload.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data != null )
        {
            filePath = data.data;
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                pictureUpload!!.setImageBitmap(bitmap)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
        }
    }