package e.caioluis.testeinloco.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.testeinloco.Constants
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.contract.CityInfoContract
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.presenter.CityInfoPresenter

class CityInfoActivity : AppCompatActivity(),
    CityInfoContract.View {

    private lateinit var presenter: CityInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_info)

        presenter = CityInfoPresenter(this)

        receiveIntentData()
    }

    private fun receiveIntentData() {

        val data = intent.getSerializableExtra(Constants.CITY_DATA) as City
        presenter.dataReceived(data)
    }
}
