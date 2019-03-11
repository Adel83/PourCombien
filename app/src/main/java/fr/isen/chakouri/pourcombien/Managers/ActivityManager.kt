package fr.isen.chakouri.pourcombien.Managers

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import fr.isen.chakouri.pourcombien.Activities.HomeActivity
import fr.isen.chakouri.pourcombien.Models.Challenge
import fr.isen.chakouri.pourcombien.Models.Player
import fr.isen.chakouri.pourcombien.Models.Round

class ActivityManager{

    companion object {

        fun backHome(context: Context): Intent =
            clearFlagActivity(
                Intent(
                    context,
                    HomeActivity::class.java
                )
            )

        private fun clearFlagActivity(intent: Intent) =
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        fun switchActivity(context: Context, activityClass: Class<*>, challengesList: ArrayList<Challenge>, playersList: ArrayList<Player>, round: Round?): Intent{
            return if(activityClass == HomeActivity::class.java){
                backHome(context)
            } else {
                var intent = Intent(context, activityClass)
                intent = makeParcelableIntent(
                    intent,
                    challengesList,
                    playersList,
                    round
                )
                intent
            }
        }

        fun switchActivityWithClearFlag(context: Context, activityClass: Class<*>, challengesList: ArrayList<Challenge>, playersList: ArrayList<Player>, round: Round?): Intent
            = makeParcelableIntent(clearFlagActivity((Intent(context, activityClass))), challengesList, playersList, round)

        private fun makeParcelableIntent(intent: Intent, challengesList: ArrayList<Challenge>, playersList: ArrayList<Player>, round: Round?): Intent{
            intent.putParcelableArrayListExtra(
                HomeActivity.CHALLENGES,
                challengesList as java.util.ArrayList<out Parcelable>
            )
            intent.putParcelableArrayListExtra(
                HomeActivity.PLAYERS,
                playersList as java.util.ArrayList<out Parcelable>
            )
            intent.putExtra(
                HomeActivity.ROUND,
                round)
            return intent
        }
    }

}