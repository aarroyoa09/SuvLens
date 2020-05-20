package net.azarquiel.suvlens.model

import java.io.Serializable

data class Camera(
    var name: String = "",
    var brand: String = "",
    var type: String = "",
    var price: Double = 0.0,
    var photo1: String = "",
    var photo2: String = "",
    var photo3: String = "",
    var pic1: String = "",
    var pic2: String = "",
    var pic3: String = "",
    var selected: Boolean = false,
    var rank: String = "",
    var rate: Double = 4.1,
    var sensor: String = "",
    var maxres: String = "",
    var maxfps: String = "",
    var iso: String = "",
    var film: String = ""
) : Serializable

data class Info(
    var name: String = "",
    var link: String = ""
) : Serializable

