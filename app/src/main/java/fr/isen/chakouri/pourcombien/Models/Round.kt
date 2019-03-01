package fr.isen.chakouri.pourcombien.Models

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

data class Round(
    var number: Int = 0,
    var challenger: Player? = null,
    var opponent: Player? = null,
    var maxNumber: Int = 0,
    var challengerNumber: Int = 0,
    var opponentNumber: Int = 0,
    var challenge: Challenge? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Player::class.java.classLoader),
        parcel.readParcelable(Player::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Challenge::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeParcelable(challenger, flags)
        parcel.writeParcelable(opponent, flags)
        parcel.writeInt(maxNumber)
        parcel.writeInt(challengerNumber)
        parcel.writeInt(opponentNumber)
        parcel.writeParcelable(challenge, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Round> {
        override fun createFromParcel(parcel: Parcel): Round {
            return Round(parcel)
        }

        override fun newArray(size: Int): Array<Round?> {
            return arrayOfNulls(size)
        }
    }

    private fun getOpponents(playersList: ArrayList<Player>) {
        if(number == 0 && playersList.size > 1){
            val randomGenerator = Random(System.currentTimeMillis())
            val indexChosen = randomGenerator.nextInt(playersList.size)
            challenger = playersList[indexChosen]
            var newIndex = randomGenerator.nextInt(playersList.size)
            while(newIndex == indexChosen){
                newIndex = randomGenerator.nextInt(playersList.size)
            }
            opponent = playersList[newIndex]
        }
    }

    fun nextRound(playersList: ArrayList<Player>, challengesList: ArrayList<Challenge>): ArrayList<Challenge>?{
        var newChallengesList: ArrayList<Challenge>? = null
        if(number >= RoundState.FINISHED.convertInt || number == RoundState.NEW.convertInt){
            // duel terminé ou nouvelle partie
            initNumber()
            getOpponents(playersList) // nouveaux opposants
            newChallengesList = getChallenge(challengesList) // nouveau challenge
        }
        ++number
        return newChallengesList
    }

    private fun initNumber() {
        number = RoundState.NEW.convertInt
        challengerNumber = 0
        opponentNumber = 0
        maxNumber = 15
    }

    private fun getChallenge(challengesList: ArrayList<Challenge>): ArrayList<Challenge>?{
        val challengeManager = ChallengeManager(challengesList)
        challenge = challengeManager.getRandomChallenge()
        return challengeManager.challengesList
    }
}