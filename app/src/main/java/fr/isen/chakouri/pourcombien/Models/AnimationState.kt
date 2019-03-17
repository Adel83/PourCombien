package fr.isen.chakouri.pourcombien.Models

class AnimationState{
    companion object STATE {
        const val NEW: Short = 0
        const val PLAYER_1: Short = 1
        const val VERSUS: Short = 2
        const val PLAYER_2: Short = 3
        const val ENDED: Short = 4

        fun animationIsEnded(state: Short) = state >= ENDED - 1
    }
}