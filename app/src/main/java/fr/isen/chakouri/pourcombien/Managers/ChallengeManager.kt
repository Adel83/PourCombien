package fr.isen.chakouri.pourcombien.Managers

import fr.isen.chakouri.pourcombien.Models.Challenge
import kotlin.random.Random

data class ChallengeManager(val challengesList: ArrayList<Challenge>? = ArrayList())
{
    fun getRandomChallenge(): Challenge?{
        return if(challengesList != null) {
            val randomGenerator = Random(System.currentTimeMillis())
            val index: Int = randomGenerator.nextInt(challengesList.size)

            val challengeSelected = challengesList[index]
            // suppression du challenge de la liste
            challengesList.remove(challengeSelected)
            challengeSelected
        } else {
            null
        }

    }

}