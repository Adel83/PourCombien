package fr.isen.chakouri.pourcombien.Models

import android.os.Parcel
import android.os.Parcelable

data class Challenge(
    val like: Int? = null,
    val content: String? = null,
    val id: String? = null,
    val idCategory: Int? = null,
    val urlImage: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(like)
        parcel.writeString(content)
        parcel.writeString(id)
        parcel.writeValue(idCategory)
        parcel.writeString(urlImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Challenge> {
        override fun createFromParcel(parcel: Parcel): Challenge {
            return Challenge(parcel)
        }

        override fun newArray(size: Int): Array<Challenge?> {
            return arrayOfNulls(size)
        }
    }
    fun getChallengeText(): String {
        return if (content != null) {
            "Pour combien tu $content"
        } else {
            ""
        }
    }
}


