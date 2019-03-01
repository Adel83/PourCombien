package fr.isen.chakouri.pourcombien.Activities

import android.app.ActionBar
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import fr.isen.chakouri.pourcombien.Models.Player
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

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

        addFieldButton.setOnClickListener{
            fieldsList.add(createEditText())
            addLine(fieldsList.last())
        }

        //button play
        buttonPlay.setOnClickListener {
            val playersList: ArrayList<Player> = createPlayersFromFields()
            if(playersList.size > 1) {
                val intent = Intent(this, ModeActivity::class.java)
                intent.putParcelableArrayListExtra(
                    HomeActivity.PLAYERS,
                    playersList as java.util.ArrayList<out Parcelable>
                )
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "2 joueurs requis minimum", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createEditText(): EditText {
        val lparams = ActionBar.LayoutParams(698, 170) // Width , height
        val edittext = EditText(this)
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
        fieldsList.add(createEditText())
        fieldsList.add(createEditText())
        fieldsList.add(createEditText())

        addLine(fieldsList[0])
        addLine(fieldsList[1])
        addLine(fieldsList[2])
    }
}