package e.caioluis.testeinloco.ui.act01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.testeinloco.Constants
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.adapter.CitiesListAdapter
import e.caioluis.testeinloco.json.Cities
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.web.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivityPresenter(

    private val context: Context,
    private val mView: MainActivityContract.IView

) : MainActivityContract.IPresenter {

    private lateinit var googleMap: GoogleMap
    private lateinit var cityAdapter: CitiesListAdapter
    private val cActivity = context as Activity
    private val bottomSheet = cActivity.findViewById<View>(R.id.frag_bottom_sheet)
    private val bSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)
    private val cityList: ArrayList<City> = ArrayList()
    private var markerLatLng = LatLng(0.0, 0.0)
    private var willShowDialog = true
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var mapFragment: SupportMapFragment =
        (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.frag_map)
                as SupportMapFragment

    override fun processPermissionResult(result: IntArray) {

        if (result.isEmpty() || result.first() == PackageManager.PERMISSION_DENIED) {

            beginAlertDialog()
            return
        }
        startApp()
    }

    private fun beginAlertDialog() {

        if (willShowDialog) {

            val builder = AlertDialog.Builder(context)

            builder.setTitle(context.getString(R.string.alert_title_permission_denied))
            builder.setMessage(context.getString(R.string.alert_message_permission_denied))
            builder.setCancelable(true)

            builder.setPositiveButton(android.R.string.yes) { _, _ ->

                willShowDialog = !askGPSPermission()
            }

            builder.setNegativeButton(context.getString(R.string.alert_button_exit)) { _, _ ->

                (mView as Activity).finish()
            }
            builder.show()
        }
    }

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

                    with(mView) {

                        showProgressBar(false)
                        showToastMessage(t.message.toString())
                    }
                }

                override fun onResponse(call: Call<Cities>, response: Response<Cities>) {

                    with(mView) {

                        val cities = response.body()

                        if (cities == null) {

                            showToastMessage(context.getString(R.string.error_message_no_nearby_cities))
                            showProgressBar(false)
                            return
                        }
                        showProgressBar(false)
                        refreshAdapterList(cities.list)
                    }
                }
            })
    }

    override fun getCityList(): ArrayList<City> {
        return cityList
    }

    private fun refreshAdapterList(list: ArrayList<City>) {

        cityList.clear()

        for (city in list) {
            cityList.add(city)
        }
        setBottomSheetState(true)
        showList()
    }

    override fun setBottomSheetConfigs() {

        bSheetBehavior.bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(view: View, slideOffset: Float) {
            }

            override fun onStateChanged(view: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                    bSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
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
            .getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: return LatLng(
            -23.533773,
            -46.625290
        )
        return LatLng(myLocation.latitude, myLocation.longitude)
    }

    override fun hasMarker(): Boolean {

        if (markerLatLng != LatLng(0.0, 0.0)) return true

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

    override fun startApp() {

        if (!askGPSPermission())
            return

        initAdapter()
        setBottomSheetConfigs()

        mapFragment.getMapAsync { map ->

            googleMap = map

            goToMyLocation(googleMap)

            googleMap.setOnMapClickListener { latLng ->

                setMapMarker(latLng, googleMap)
            }
        }
    }

    private fun initAdapter() {
        cityAdapter = CitiesListAdapter(
            context,
            R.layout.list_item,
            getCityList()
        )
        cActivity.bottom_sheet_lv_cities.adapter = cityAdapter
    }

    override fun showList() {
        cityAdapter.notifyDataSetChanged()
    }

    override fun askGPSPermission(): Boolean {

        return if (!hasGpsPermission()) {
            requestGPSPermission()
            false
        } else true
    }

    override fun searchClicked() {

        if (hasMarker()) {
            startApiRequest()
        }
    }

    override fun openListClicked() {
        setBottomSheetState(true)
    }

    override fun closeClicked() {
        setBottomSheetState(false)
    }
}
