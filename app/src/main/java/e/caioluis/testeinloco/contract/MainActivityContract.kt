package e.caioluis.testeinloco.contract

import e.caioluis.testeinloco.json.City

interface MainActivityContract {

    interface IView {

        fun showProgressBar(show: Boolean)
        fun execNavigation(data: City)
        fun showSnackBarMessage(text: String)
    }

    interface IPresenter {

        fun processPermissionResult(result: IntArray)
        fun getCityData(city: City)
        fun openListClicked()
        fun searchClicked()
        fun closeClicked()
        fun startApp()
    }
}