package e.caioluis.testeinloco.web

import e.caioluis.testeinloco.json.Cities
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebAPI {

    @GET ("data/2.5/find?cnt=15&APPID=e63364042c3b85e0037b30c4cf65cb68")

    fun syncCallNearbyCitiesByLatLong(
        @Query("lat") latitude : Double,
        @Query("lon") longitude : Double
    ) : Call<Cities>
}