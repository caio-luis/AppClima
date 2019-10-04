package e.caioluis.testeinloco.ui.act02

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.testeinloco.Constants
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.json.City

class CityInfoActivity : AppCompatActivity(), CityInfoContract.View {

    private lateinit var context: Context
    private lateinit var presenter: CityInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_info)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@CityInfoActivity
        presenter = CityInfoPresenter(this)
    }

    private fun initActions() {

        val data = intent.getSerializableExtra(Constants.CITY_DATA) as City

        presenter.dataReceived(data)
    }
}


