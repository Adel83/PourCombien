package fr.isen.chakouri.pourcombien.Models

import android.os.Parcel
import android.os.Parcelable

data class Round(
    var number: Int = 0,
    var challenger: Player? = null,
    var opponent: Player? = null,
    var maxNumber: Int = 0,
    var challengerNumber: Int = 0,
    var opponentNumber: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Player::class.java.classLoader),
        parcel.readParcelable(Player::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeParcelable(challenger, flags)
        parcel.writeParcelable(opponent, flags)
        parcel.writeInt(maxNumber)
        parcel.writeInt(challengerNumber)
        parcel.writeInt(opponentNumber)
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
}