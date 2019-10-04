package e.caioluis.testeinloco.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cities(

    @SerializedName("list")
    val list: ArrayList<City>

) : Serializable





