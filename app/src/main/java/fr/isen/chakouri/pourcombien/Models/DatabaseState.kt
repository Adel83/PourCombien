package fr.isen.chakouri.pourcombien.Models

class DatabaseState{
    companion object Like {
        const val ACTION_CANCELLED = 1
        const val READ_LIKE_FAILURE = 2
        const val UPDATE_LIKE_FAILURE = 3
        const val UPDATE_LIKE_SUCCESS = 4
        const val UPDATE_UNLIKE_SUCCESS = 5
    }
}