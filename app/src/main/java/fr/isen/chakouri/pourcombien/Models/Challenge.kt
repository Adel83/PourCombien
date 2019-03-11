package fr.isen.chakouri.pourcombien.Models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fr.isen.chakouri.pourcombien.Activities.QuestionActivity

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

    fun updateNumberOfLikes(number: Int, myRef: DatabaseReference?, instance: QuestionActivity){
        // lecture de la dernière valeur de like
        var lastNumberOfLikes: Int? = like
        val messageListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    lastNumberOfLikes = dataSnapshot.getValue(Int::class.java)
                    if(lastNumberOfLikes != null){
                        val completionListener =
                            DatabaseReference.CompletionListener { p0, _ ->
                                if(p0 == null){
                                    // succès de l'opération
                                    if(number > 0)
                                        instance.onLikeAction(DatabaseState.UPDATE_LIKE_SUCCESS)
                                    else
                                        instance.onLikeAction(DatabaseState.UPDATE_UNLIKE_SUCCESS)
                                } else{
                                    instance.onLikeAction(DatabaseState.UPDATE_LIKE_FAILURE)
                                }
                            }
                        myRef?.setValue(lastNumberOfLikes!! + number, completionListener)
                    }
                }}
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                instance.onLikeAction(DatabaseState.ACTION_CANCELLED)
            }
        }
        myRef?.addListenerForSingleValueEvent(messageListener)
    }
}


