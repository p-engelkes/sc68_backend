package com.pengelkes.service

/**
 * Created by pengelkes on 23.12.2016.
 */
enum class Position(val translation: String) {
    GOALKEEPER("Torwart"),
    DEFENSE("Abwehr"),
    MIDFIELD("Mittelfeld"),
    OFFENSE("Sturm"),
    COACH("Trainer"),
    SUPERVISOR("Betreuer"),
    FAN("Fan");

    companion object {
        fun fromTranslation(translation: String): Position? {
            Position.values().forEach {
                if (it.translation == translation) {
                    return it
                }
            }

            return null
        }
    }
}