package fr.isen.chakouri.pourcombien.Activities

import android.animation.ValueAnimator
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintSet
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.Managers.SoundManager
import fr.isen.chakouri.pourcombien.Managers.TTCManager
import fr.isen.chakouri.pourcombien.Models.*
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_versus.*

class VersusActivity : AppCompatActivity() {

    private var challengesList: ArrayList<Challenge>? = null
    private var playersList: ArrayList<Player>? = null
    private var round = Round(RoundState.ONNEW.convertInt) // initialisation de base à ONNEW
    private lateinit var ttcManager: TTCManager

    // champs liés à l'animation
    private var constraintSet = ConstraintSet()
    private var autoTransition = AutoTransition()
    private var animationState: Short = AnimationState.NEW
    private var soundManager = SoundManager(this)
    private var endAnimation: Boolean = false
    private var challengeAnnunciated: Boolean = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_versus)

        // récupération des challenges et de la des joueurs
        intent?.let {
            challengesList = intent.getParcelableArrayListExtra(HomeActivity.CHALLENGES)
            playersList = intent.getParcelableArrayListExtra(HomeActivity.PLAYERS)
            round = intent.getParcelableExtra(HomeActivity.ROUND)
        }

        if(challengesList != null)
        {
            if(playersList != null)
            {
                if(round.number == RoundState.ONNEW.convertInt)
                    challengesList = round.nextRound(playersList!!, challengesList!!)
                player1Field.text = round.challenger?.username
                player2Field.text = round.opponent?.username

                ttcManager = TTCManager(this)
            }
        }
        else{
            Toast.makeText(this, "Plus de challenges", Toast.LENGTH_SHORT).show()
        }

        // clone des contraintes de base
        constraintSet.clone(Layout)
        soundManager.playSound(SoundManager.ANIMATION_BACKGROUND)
        initAutoTransition()
        blinkInstructions()

        Layout.setOnClickListener {
            when(animationState){
                AnimationState.NEW ->
                {
                    // suppression de l'instruction
                    blinkInstructions.clearAnimation()
                    blinkInstructions.visibility = View.GONE
                    // lancement de l'animation
                    anim()
                }
                in shortArrayOf(AnimationState.PLAYER_1, AnimationState.VERSUS, AnimationState.PLAYER_2) -> {
                    forceDisplay()
                }
                AnimationState.ENDED ->
                {
                    soundManager.releaseAllSounds()
                    round.number = RoundState.ONNEW.convertInt
                    startActivity(
                        ActivityManager.switchActivity(this, QuestionActivity::class.java,
                            challengesList!!, playersList!!, round))
                }
            }
            animationState++
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initAutoTransition(){
        autoTransition.duration = 1500
        autoTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
                Toast.makeText(applicationContext, "CANCELLED :(", Toast.LENGTH_SHORT).show()
            }

            override fun onTransitionStart(transition: Transition?) {
            }

            override fun onTransitionEnd(transition: Transition?) {
                if(endAnimation) {
                    animationState = AnimationState.ENDED
                    if(!challengeAnnunciated) {
                        ttcManager.initTTC(TTCManager.PROVOCATION, round)
                        challengeAnnunciated = true
                    }
                }
                else {
                    if (!AnimationState.animationIsEnded(animationState)) {
                        anim()
                        animationState++
                    } else {
                        animationState = AnimationState.ENDED
                        if(!challengeAnnunciated) {
                            ttcManager.initTTC(TTCManager.PROVOCATION, round)
                            challengeAnnunciated = true
                        }
                    }
                }
            }
        })
    }

    private fun anim() {
        soundManager.playSound(SoundManager.IMPACT)
        when(animationState) {
            AnimationState.NEW ->
            {
                TransitionManager.beginDelayedTransition(Layout, autoTransition)
                constraintSet.setVisibility(player1Field.id, ConstraintSet.VISIBLE)
                constraintSet.applyTo(Layout)
            }

            AnimationState.PLAYER_1 ->
            {
                TransitionManager.beginDelayedTransition(Layout, autoTransition)
                constraintSet.setVisibility(imageVersus.id, ConstraintSet.VISIBLE)
                constraintSet.applyTo(Layout)
            }
            AnimationState.VERSUS ->
            {
                TransitionManager.beginDelayedTransition(Layout, autoTransition)
                constraintSet.setVisibility(player2Field.id, ConstraintSet.VISIBLE)
                constraintSet.applyTo(Layout)
            }
        }
    }

    private fun forceDisplay(){
        endAnimation = true
        autoTransition.excludeTarget(player1Field, true)
        autoTransition.duration = 100
        TransitionManager.beginDelayedTransition(Layout, autoTransition)
        constraintSet.setVisibility(player1Field.id, ConstraintSet.VISIBLE)
        constraintSet.setVisibility(imageVersus.id, ConstraintSet.VISIBLE)
        constraintSet.setVisibility(player2Field.id, ConstraintSet.VISIBLE)
        constraintSet.applyTo(Layout)
    }

    private fun blinkInstructions(){
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 1000L
        alphaAnimation.repeatCount = ValueAnimator.INFINITE
        alphaAnimation.repeatMode = ValueAnimator.REVERSE
        alphaAnimation.fillAfter = false
        blinkInstructions.startAnimation(alphaAnimation)
    }

    override fun onPause(){
        ttcManager.stopSpeech()
        soundManager.pauseAllSounds()
        super.onPause()
    }

    override fun onResume(){
        soundManager.restartAllSounds()
        super.onResume()
    }

    override fun onBackPressed() {
        round.number = RoundState.ONWAITING.convertInt
        challengeAnnunciated = true // évite d'annoncer le challenge sur la page précédente
        forceDisplay()
        soundManager.releaseAllSounds()
        ttcManager.stopSpeech()
        super.onBackPressed()
    }
}