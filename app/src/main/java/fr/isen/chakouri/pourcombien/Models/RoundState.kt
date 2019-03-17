package fr.isen.chakouri.pourcombien.Models

enum class RoundState(val convertInt: Int) {
    ONWAITING(-1),
    ONSUCCESS(0),
    ONNEW(1),
    ONGOING(2),
    ONFAILURE(3)
}