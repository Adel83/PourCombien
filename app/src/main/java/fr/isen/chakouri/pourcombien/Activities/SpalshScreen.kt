package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import fr.isen.chakouri.pourcombien.R

class SpalshScreen : AppCompatActivity() {

    private var mDelayHandler: Handler?= null
    private val SPLASH_DELAY: Long = 2700 //2,7 second

    internal  val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            val intent = Intent(applicationContext,
                HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        if(mDelayHandler != null){
            mDelayHandler!!.removeCallbacks(mRunnable)
        }


        super.onDestroy()
    }

}
