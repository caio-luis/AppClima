package e.caioluis.testeinloco.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.adapter.CitiesListAdapter
import e.caioluis.testeinloco.json.Cities
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.web.Constants
import e.caioluis.testeinloco.web.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    lateinit var mapFragment: SupportMapFragment

    lateinit var bSheetBehavior: BottomSheetBehavior<View>

    lateinit var googleMap: GoogleMap
    lateinit var context: Context

    lateinit var cityAdapter: CitiesListAdapter

    var citiesList = ArrayList<City>()

    private var markerLatLng = LatLng(0.0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@MainActivity

        val bottomSheet : View = findViewById(R.id.frag_bottom_sheet)
        bSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        mapFragment = supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    }

    @SuppressLint("MissingPermission")
    private fun initActions() {

        if (!hasGPSPermission()) {

            requestGPSPermission()
            return
        }

        mapFragment.getMapAsync {

            googleMap = it

            googleMap.isMyLocationEnabled = true

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getMyLocationLatLng(), 10f))

            googleMap.setOnMapClickListener {

                markerLatLng = it

                googleMap.clear()

                googleMap.addMarker(MarkerOptions().position(it).title("teste de click"))
            }
        }

        mapfrag_btn_search.setOnClickListener {

            if (markerLatLng == LatLng(0.0,0.0)){

                showToastMessage("Selecione um ponto no mapa!")

                return@setOnClickListener
            }

            bSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

                override fun onSlide(view: View, slideOffset: Float) {

                }

                override fun onStateChanged(view: View, newState : Int) {

                    if (newState == BottomSheetBehavior.STATE_EXPANDED){

                        bSheetBehavior.isHideable = false
                    }

                }
            })

            beginAPIRequest()
            bottomSheetVisibilityState(true)
        }

        bottom_sheet_lv_cities.setOnItemClickListener { parent, view, position, id ->


        }
    }

    private fun hasGPSPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGPSPermission() {
        val permission =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(
            this,
            permission,
            0
        )
    }

    private fun beginAPIRequest() {

        val webAPI = retrofitClient()

        webAPI.syncCallNearbyCitiesByLatLong(markerLatLng.latitude, markerLatLng.longitude)
            .enqueue(object : Callback<Cities> {

                override fun onFailure(call: Call<Cities>, t: Throwable) {

                    showToastMessage(t.message.toString())
                }

                override fun onResponse(call: Call<Cities>, response: Response<Cities>) {

                    val response = response.body()

                    citiesList = response!!.list

                    cityAdapter = CitiesListAdapter(
                        context,
                        R.layout.list_item,
                        citiesList
                    )
                    bottom_sheet_lv_cities.adapter = cityAdapter
                }
            })
    }

    fun showToastMessage(message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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

    @SuppressLint("MissingPermission")
    private fun getMyLocationLatLng() : LatLng {

        val myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val myLocationLatLng = LatLng(myLocation!!.latitude, myLocation.longitude)

        return myLocationLatLng
    }

    private fun bottomSheetVisibilityState(state: Boolean){

        if (state)
            bSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        else
            bSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

    }
}
