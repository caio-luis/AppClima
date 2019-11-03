package e.caioluis.testeinloco.presenter

import android.app.Activity
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.contract.CityInfoContract
import kotlinx.android.synthetic.main.activity_city_info.*

class CityInfoPresenter(
    view: CityInfoContract.View

) : CityInfoContract.Presenter {

    private lateinit var mCity: City
    private val activity = view as Activity

    override fun dataReceived(city: City) {

        with(city) {

            temperature.temp_max.let { tempMax ->
                temperature.celsius_max = kelvinToCelsius(tempMax)
            }

            temperature.temp_min.let { tempMin ->
                temperature.celsius_min = kelvinToCelsius(tempMin)
            }

            weather.first().let {
                description = it.description
            }
        }
        mCity = city
        setTexts()
    }

    private fun setTexts() {
        with(activity, {
            with(mCity) {
                info_tv_city_name.text = name
                info_tv_weather_desc.text = description
                info_tv_temp_max.text = temperature.celsius_max
                info_tv_temp_min.text = temperature.celsius_min
            }
        })
    }

    override fun kelvinToCelsius(value: Double): String {

        return "${"%.0f".format((value - 273.15))}ºC"
    }
}