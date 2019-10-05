package e.caioluis.testeinloco.ui.act01

import e.caioluis.testeinloco.json.City

interface MainActivityContract {

    interface IView {

        fun showToastMessage(message: String)
        fun showProgressBar(show: Boolean)
        fun execNavigation(data: City)
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