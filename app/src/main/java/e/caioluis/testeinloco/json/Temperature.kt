package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Temperature (

    @SerializedName("temp_min")
    val temp_min : Double,
    @SerializedName("temp_max")
    val temp_max : Double,

    var celcius_min: String,
    var celcius_max: String

): Serializable