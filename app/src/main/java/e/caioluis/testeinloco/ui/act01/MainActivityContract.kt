package e.caioluis.testeinloco.ui.act01

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import e.caioluis.testeinloco.json.City

interface MainActivityContract {

    interface IView {

        fun showToastMessage(message: String)
        fun showList()
        fun execNav(data: City)
        fun showProgressBar(state: Boolean)
        fun askGPSPermission()
        fun startApp()
    }

    interface IPresenter {

        fun getCityList() : ArrayList<City>
        fun showGPSPermissionDialog()
        fun processPermissionResult(result : IntArray)
        fun hasGpsPermission(): Boolean
        fun requestGPSPermission()
        fun goToMyLocation(googleMap: GoogleMap)
        fun setMapMarker(markerLatLgn: LatLng, googleMap: GoogleMap)
        fun startApiRequest()
        fun setBottomSheetState(state: Boolean)
        fun setBottomSheetConfigs()
        fun hasMarker(): Boolean
        fun getCityData(city: City)
    }
}