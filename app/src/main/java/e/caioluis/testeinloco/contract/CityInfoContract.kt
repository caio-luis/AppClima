package e.caioluis.testeinloco.contract

import e.caioluis.testeinloco.json.City

interface CityInfoContract {

    interface View

    interface Presenter {

        fun dataReceived(city: City)
        fun kelvinToCelsius(value: Double): String
    }
}