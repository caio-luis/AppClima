package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cities(

    @SerializedName("list")
    val list : ArrayList<City>

): Serializable

data class City (

    @SerializedName("id")
    val id : Int,
    @SerializedName("weather")
    val weather : ArrayList<Weather>,
    @SerializedName("main")
    val temperature : Temperature,
    @SerializedName("name")
    val name : String,

    var description: String

)  : Serializable

data class Weather(

    @SerializedName("description")
    val description: String

) : Serializable

data class Temperature (

    @SerializedName("temp_min")
    val temp_min : Double,
    @SerializedName("temp_max")
    val temp_max : Double,

    var celcius_min: String,
    var celcius_max: String

): Serializable
