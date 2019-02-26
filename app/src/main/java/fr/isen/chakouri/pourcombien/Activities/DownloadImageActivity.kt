package fr.isen.chakouri.pourcombien.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.chakouri.pourcombien.R

class DownloadImageActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_image)
        loadWithPicasso()
    }

    private fun loadWithPicasso() {

        // ImageView in your Activity
        val imageView = findViewById<ImageView>(R.id.imageView)
        val url = "gs://pour-combien-tu.appspot.com/images/5bb03c04-a664-42f9-8efb-88caabbce93a"
        Picasso
            .get()
            .load(url)
            .into(imageView)
        // [END storage_load_with_glide]
    }
}
