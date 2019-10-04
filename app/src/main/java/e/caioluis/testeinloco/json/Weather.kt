package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Weather(

    @SerializedName("description")
    val description: String

) : Serializable