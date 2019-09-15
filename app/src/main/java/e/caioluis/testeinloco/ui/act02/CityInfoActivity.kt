package e.caioluis.testeinloco.ui.act02

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.json.City
import kotlinx.android.synthetic.main.activity_city_info.*

class CityInfoActivity : AppCompatActivity(), CityInfoContract.CView {

    private lateinit var context: Context
    private lateinit var cPresenter : CityInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_info)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@CityInfoActivity

        cPresenter = CityInfoPresenter(this)
    }

    private fun initActions() {

        val cityData = intent.getSerializableExtra("data") as City

        cPresenter.processData(cityData)
    }

    override fun setOnTextView(city: City) {

        info_tv_city_name.text = city.name
        info_tv_weather_desc.text = city.description
        info_tv_temp_max.text = city.temp.celcius_max
        info_tv_temp_min.text = city.temp.celcius_min
    }
}


