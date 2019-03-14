package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import fr.isen.chakouri.pourcombien.Models.Player
import android.view.ViewGroup
import android.widget.*
import fr.isen.chakouri.pourcombien.Managers.ActivityManager
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val CHALLENGES = "challenges"
        const val PLAYERS = "players"
        const val ROUND = "round"
    }

    var numberOfLines = 1
    var fieldsList: ArrayList<EditText> = ArrayList()
    var deleteList: java.util.ArrayList<ImageView> = ArrayList()
    var layoutsList: ArrayList<LinearLayout> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(fr.isen.chakouri.pourcombien.R.layout.activity_home)
        initTwoFields()

        //button lifecycle
        addchallenge.setOnClickListener(this)

        // ajout de joueurs
        addFieldButton.setOnClickListener(this)
        //button play
        buttonPlay.setOnClickListener(this)

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
                deleteList.add(createDeleteImage())
                layoutsList.add(createLinearLayout())
                addLine(fieldsList.last(), layoutsList.last(), deleteList.last())
            }
            in deleteList -> {
                //Toast.makeText(this, "supprime", Toast.LENGTH_SHORT).show()
                val index = deleteList.indexOf(v!!)
                // disparation du layout
                fieldsList[index+2].visibility = View.GONE
                layoutsList[index+2].visibility = View.GONE
                v.visibility = View.GONE
                // effacement du tableau
                fieldsList.remove(fieldsList[index+2])
                layoutsList.remove(layoutsList[index+2])
                deleteList.remove(deleteList[index])
                updateHintNames()
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
            addchallenge ->
            {
                val intent = Intent(this, AddChallengeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun createEditText(): EditText {
        val editText = EditText(this)
        editText.setTextColor(Color.WHITE)
        editText.setHintTextColor(Color.GRAY)
        editText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        editText.hint = " Joueur $numberOfLines"
        numberOfLines++
        return editText
    }

    private fun createDeleteImage() : ImageView {
        val image = ImageView(this)
        image.setImageResource(R.drawable.ic_delete)
        return image
    }

    private fun addLine(editText: EditText, linearLayout: LinearLayout, image: ImageView?) {
        // insertion du LinearLayout
        joueursLayout.addView(linearLayout)

        var p = LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, if(image == null) 4f else 3f)
        editText.layoutParams = p
        editText.id = numberOfLines + 1
        linearLayout.addView(editText)

        if(image != null) {
            p = LinearLayout.LayoutParams(1, 50, 1f)
            image.layoutParams = p
            image.id = numberOfLines + 1
            linearLayout.addView(image)
            image.setOnClickListener(this)
        }
    }

    private fun createLinearLayout(): LinearLayout {
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.id = numberOfLines + 1
        linearLayout.weightSum = 4f
        linearLayout.gravity = Gravity.CENTER_VERTICAL
        return linearLayout
    }

    private fun createPlayersFromFields(): ArrayList<Player> {
        val playersList = ArrayList<Player>()
        for((id, field) in fieldsList.withIndex()){
            if(!field.text.isNullOrBlank())
                playersList.add(Player(id, field.text.toString()))
        }
        return playersList
    }

    private fun updateHintNames(){
        for((index, editText) in fieldsList.withIndex()){
            if(editText.text.isNullOrBlank()){
                editText.hint = "Joueur ${index+1}"
            }
        }
    }

    private fun initTwoFields() {
        for(i in 0..1){
            fieldsList.add(createEditText())
            layoutsList.add(createLinearLayout())
            addLine(fieldsList[i], layoutsList[i],null)
        }
    }
}