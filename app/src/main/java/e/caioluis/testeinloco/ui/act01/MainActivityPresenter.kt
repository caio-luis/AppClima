package e.caioluis.testeinloco.ui.act01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.testeinloco.Constants
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.json.Cities
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.web.WebAPI
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivityPresenter(

    private val context: Context,
    private val mView: MainActivityContract.IView,
    bottomSheet: View

) : MainActivityContract.IPresenter {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val bSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)

    private var markerLatLng = LatLng(0.0, 0.0)

    override fun hasGpsPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestGPSPermission() {

        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(
            context as Activity,
            permission,
            0
        )
    }

    override fun setMapMarker(markerLatLgn: LatLng, googleMap: GoogleMap) {
        markerLatLng = markerLatLgn

        googleMap.clear()

        googleMap.addMarker(MarkerOptions().position(markerLatLgn))
    }

    override fun startApiRequest() {

        mView.showProgressBar(true)

        val webAPI = retrofitClient()

        webAPI.syncCallNearbyCitiesByLatLong(markerLatLng.latitude, markerLatLng.longitude)
            .enqueue(object : Callback<Cities> {

                override fun onFailure(call: Call<Cities>, t: Throwable) {

                    mView.showProgressBar(false)

                    mView.showToastMessage(t.message.toString())
                }

                override fun onResponse(call: Call<Cities>, response: Response<Cities>) {

                    val cities = response.body()

                    if (cities == null) {

                        mView.showToastMessage(context.getString(R.string.error_message_no_nearby_cities))

                        mView.showProgressBar(false)

                        return
                    }

                    mView.showProgressBar(false)
                    mView.showList(cities.list)
                }
            })
    }

    override fun setBottomSheetConfigs() {

        bSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(view: View, slideOffset: Float) {
            }

            override fun onStateChanged(view: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                    bSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })
    }

    override fun setBottomSheetState(state: Boolean) {

        if (state)
            bSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        else
            bSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    @SuppressLint("MissingPermission")
    override fun goToMyLocation(googleMap: GoogleMap) {

        googleMap.isMyLocationEnabled = true

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getMyLocationLatLng(), 10f))
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocationLatLng(): LatLng {

        // centro de São Paulo
        val myLocation = locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: return LatLng(-23.533773, -46.625290)

        return LatLng(myLocation.latitude, myLocation.longitude)
    }

    override fun hasMarker(): Boolean {

        if (markerLatLng != LatLng(0.0, 0.0)) {

            return true
        }
        mView.showToastMessage("Selecione um ponto no mapa!")

        return false
    }

    override fun getCityData(city: City) {

        mView.execNav(city)
    }

    private fun retrofitClient(): WebAPI {

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retroFitAPI = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).client(client)
            .build()

        return retroFitAPI.create(WebAPI::class.java)
    }
}
