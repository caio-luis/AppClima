package e.caioluis.testeinloco.ui.act02

import android.app.Activity
import e.caioluis.testeinloco.json.City
import kotlinx.android.synthetic.main.activity_city_info.*

class CityInfoPresenter(

    private val view: CityInfoContract.View

) : CityInfoContract.Presenter {

    lateinit var mCity: City

    override fun dataReceived(city: City) {

        city.temperature.temp_max.let { tempMax ->

            city.temperature.celsius_max = kelvinToCelsius(tempMax)
        }

        city.temperature.temp_min.let { tempMin ->

            city.temperature.celsius_min = kelvinToCelsius(tempMin)
        }

        city.weather.first().let {
            city.description = it.description
        }

        mCity = city
        setTexts()
    }

    private fun setTexts() {

        val activity = view as Activity

        with(activity) {

            info_tv_city_name.text = mCity.name
            info_tv_weather_desc.text = mCity.description
            info_tv_temp_max.text = mCity.temperature.celsius_max
            info_tv_temp_min.text = mCity.temperature.celsius_min
        }
    }

    override fun kelvinToCelsius(value: Double): String {

        return "${"%.0f".format((value - 273.15))}ºC"
    }
}