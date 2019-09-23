package e.caioluis.testeinloco.ui.act01

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import e.caioluis.testeinloco.json.City

interface MainActivityContract {

    interface IView {

        fun showToastMessage(message: String)
        fun showProgressBar(state: Boolean)
        fun execNav(data: City)
    }

    interface IPresenter {

        fun setMapMarker(markerLatLgn: LatLng, googleMap: GoogleMap)
        fun processPermissionResult(result : IntArray)
        fun goToMyLocation(googleMap: GoogleMap)
        fun setBottomSheetState(state: Boolean)
        fun getCityList() : ArrayList<City>
        fun listItemClicked(position: Int)
        fun askGPSPermission() : Boolean
        fun hasGpsPermission(): Boolean
        fun showGPSPermissionDialog()
        fun setBottomSheetConfigs()
        fun getCityData(city: City)
        fun requestGPSPermission()
        fun hasMarker(): Boolean
        fun startApiRequest()
        fun openListClicked()
        fun searchClicked()
        fun closeClicked()
        fun showList()
        fun startApp()
    }
}