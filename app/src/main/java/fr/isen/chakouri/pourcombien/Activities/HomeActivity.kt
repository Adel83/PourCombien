package fr.isen.chakouri.pourcombien.Activities

import android.app.ActionBar
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.isen.chakouri.pourcombien.Models.Player
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val CHALLENGES = "challenges"
        const val PLAYERS = "players"
        const val ROUND = "round"
    }

    var numberOfLines = 1
    var fieldsList: ArrayList<EditText> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(fr.isen.chakouri.pourcombien.R.layout.activity_home)
        initThreeFields()

        // ajout de joueurs
        addFieldButton.setOnClickListener(this)
        //button play
        buttonPlay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            addFieldButton ->
            {
                // TODO : ajouter la suppresion d'un EditText
                fieldsList.add(createEditText())
                addLine(fieldsList.last())
            }
            buttonPlay ->
            {
                val playersList: ArrayList<Player> = createPlayersFromFields()
                if(playersList.size > 1) {
                    /*val intent = Intent(this, ModeActivity::class.java)
                    intent.putParcelableArrayListExtra(
                        HomeActivity.PLAYERS,
                        playersList as java.util.ArrayList<out Parcelable>
                    )*/
                    startActivity(ActivityManager.switchActivity(this, ModeActivity::class.java, ArrayList(), playersList, null))
                }
                else {
                    Toast.makeText(this, "2 joueurs requis minimum", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createEditText(): EditText {
        val lparams = ActionBar.LayoutParams(498, 170) // Width , height
        val edittext = EditText(this)
        edittext.setTextColor(Color.WHITE)
        edittext.setHintTextColor(Color.GRAY)
        edittext.textAlignment = View.TEXT_ALIGNMENT_CENTER
        edittext.layoutParams = lparams
        edittext.hint = " Joueur $numberOfLines"
        numberOfLines++
        return edittext
    }

    fun addLine(editText: EditText) {
        val p = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.layoutParams = p
        editText.id = numberOfLines + 1
        joueursLayout.addView(editText)
    }

    private fun createPlayersFromFields(): ArrayList<Player> {
        val playersList = ArrayList<Player>()
        for((id, field) in fieldsList.withIndex()){
            if(!field.text.isNullOrBlank())
                playersList.add(Player(id, field.text.toString()))
        }
        return playersList
    }

    private fun initThreeFields() {
        for(i in 0..2){
            fieldsList.add(createEditText())
            addLine(fieldsList[i])
        }
    }
}