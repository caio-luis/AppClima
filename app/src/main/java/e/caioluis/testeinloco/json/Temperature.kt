package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Temperature(

    @SerializedName("temp_min")
    val temp_min: Double,
    @SerializedName("temp_max")
    val temp_max: Double,

    var celsius_min: String,
    var celsius_max: String

) : Serializable