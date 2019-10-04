package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class City(

    @SerializedName("id")
    val id: Int,
    @SerializedName("weather")
    val weather: ArrayList<Weather>,
    @SerializedName("main")
    val temperature: Temperature,
    @SerializedName("name")
    val name: String,

    var description: String

) : Serializable