package fr.isen.chakouri.pourcombien.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val CHALLENGES = "challenges"
        const val PLAYERS = "players"
        const val ROUND = "round"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //button play
        buttonPlay.setOnClickListener {
            if(numberOfPlayersRight()) {
                val playersList: ArrayList<Player> = createPlayersFromFields()
                val intent = Intent(this, ModeActivity::class.java)
                intent.putParcelableArrayListExtra(HomeActivity.PLAYERS,
                    playersList as java.util.ArrayList<out Parcelable>
                )
                startActivity(intent)
            }
        }

    }

    private fun createPlayersFromFields(): ArrayList<Player> {
        val playersList = ArrayList<Player>()
        if(!name1Field.text.isNullOrBlank())
            playersList.add(Player(1,name1Field.text.toString()))
        if(!name2Field.text.isNullOrBlank())
            playersList.add(Player(2, name2Field.text.toString()))
        return playersList
    }

    private fun numberOfPlayersRight(): Boolean =
        !name1Field.text.isNullOrBlank() && !name2Field.text.isNullOrBlank()
}