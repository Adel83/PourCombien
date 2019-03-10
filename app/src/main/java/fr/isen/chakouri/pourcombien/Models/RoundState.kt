package fr.isen.chakouri.pourcombien.Models

enum class RoundState(val convertInt: Int) {
    ONSUCCESS(0),
    ONNEW(1),
    ONGOING(2),
    ONFAILURE(3)
}