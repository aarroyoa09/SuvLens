package net.azarquiel.suvlens.model

import java.io.Serializable

data class Camera(
    var name: String = "",
    var brand: String = "",
    var type: String = "",
    var price: Double = 0.0,
    var photo: String = "",
    var photo2: String = "",
    var photo3: String = "",
    var pic1: String = "",
    var pic2: String = "",
    var pic3: String = "",
    var selected: Boolean = false,
    var rank: String = "",
    var rate: Float = 4.1F
//    var sensor: String,
//    var maxres: String,
//    var iso: String,
//    var film: String
) : Serializable

data class Tipo(
    var name: String = "",
    var photo: String = ""
) : Serializable

data class Marca(
    var name: String = "",
    var photom: String = ""
) : Serializable

data class Precio(
    var rango: Double = 0.0,
    var name: String = "",
    var photo: String = ""
) : Serializable