package fr.isen.chakouri.pourcombien.Activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private var mDelayHandler: Handler?= null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private val soundManager = SoundManager(this)

    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            val intent = Intent(applicationContext,
                HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        blinkTajj()
        soundManager.playSound(SoundManager.INTRO)
    }

    public override fun onDestroy() {
        if(mDelayHandler != null){
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        soundManager.releaseAllSounds()
        tajjText.clearAnimation()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun blinkTajj(){
        val objectAnimator = ObjectAnimator.ofObject(tajjText, "textColor", ArgbEvaluator(), ContextCompat.getColor(this, R.color.colorPrimary), ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorPrimaryDark))
        objectAnimator.duration = 1000L
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
    }

}
