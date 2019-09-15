package e.caioluis.testeinloco.ui.act02

import e.caioluis.testeinloco.json.City

class CityInfoPresenter (

    private val cView : CityInfoContract.CView

) : CityInfoContract.CPresenter {

    override fun processData(city: City) {

        city.temperature.temp_max.let {

            city.temperature.celcius_max = kelvinToCelcius(it)
        }

        city.temperature.temp_min.let {

            city.temperature.celcius_min = kelvinToCelcius(it)
        }

        city.weather.first().let {
            city.description = it.description
        }

        cView.setOnTextView(city)
    }

    override fun kelvinToCelcius(value: Double): String {

        return "${
        "%.0f".format(
            (value - 273.15)
        )}ºC"
    }
}