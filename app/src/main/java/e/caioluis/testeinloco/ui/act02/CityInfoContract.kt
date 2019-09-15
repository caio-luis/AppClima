package e.caioluis.testeinloco.ui.act02

import e.caioluis.testeinloco.json.City

interface CityInfoContract {

    interface CView {

        fun setOnTextView(city: City)
    }

    interface CPresenter {

        fun processData(city: City)
        fun kelvinToCelcius(value: Double): String
    }
}