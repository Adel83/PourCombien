package fr.isen.chakouri.pourcombien.Activities

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.isen.chakouri.pourcombien.Models.Player
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.Button
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
    var deleteList: java.util.ArrayList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(fr.isen.chakouri.pourcombien.R.layout.activity_home)
        initTwoFields()

        // ajout de joueurs
        addFieldButton.setOnClickListener(this)
        //button play
        buttonPlay.setOnClickListener(this)

        //supression




        //Rules
        rulesButton.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            addFieldButton ->
            {
                // TODO : ajouter la suppresion d'un EditText
                fieldsList.add(createEditText())
                deleteList.add(createButton())
                addLine(fieldsList.last(),deleteList.last())
            }
            in deleteList -> {
                //Toast.makeText(this, "supprime", Toast.LENGTH_SHORT).show()
                val index = deleteList.indexOf(v!!)
                fieldsList[index+2].visibility = View.GONE
                v?.visibility = View.GONE
                fieldsList.remove(fieldsList[index+2])

                deleteList[index].visibility = View.GONE
                v?.visibility = View.GONE
                deleteList.remove(deleteList[index])
                --numberOfLines
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

    private fun createButton() : Button {
        val buttonParam = ActionBar.LayoutParams(16, 16) // Width , height
        val button = Button(this)
        button.setText("Delete")
        return button
    }

    fun addLine(editText: EditText, button: Button?) {
        val p = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.layoutParams = p
        editText.id = numberOfLines + 1
        joueursLayout.addView(editText)

        if (button != null){
        button.setOnClickListener(this)
        button.layoutParams = p
        button.id = numberOfLines + 1
        joueursLayout.addView(button)
        }
    }

    private fun createPlayersFromFields(): ArrayList<Player> {
        val playersList = ArrayList<Player>()
        for((id, field) in fieldsList.withIndex()){
            if(!field.text.isNullOrBlank())
                playersList.add(Player(id, field.text.toString()))
        }
        return playersList
    }

    private fun initTwoFields() {
        for(i in 0..1){
            fieldsList.add(createEditText())
            addLine(fieldsList[i],null)
        }
    }
}