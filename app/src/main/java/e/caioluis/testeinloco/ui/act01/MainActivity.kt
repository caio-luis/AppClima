package e.caioluis.testeinloco.ui.act01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.json.City
import e.caioluis.testeinloco.ui.act02.CityInfoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, MainActivityContract.IView {

    private lateinit var context: Context
    private lateinit var presenter: MainActivityContract.IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@MainActivity

        presenter = MainActivityPresenter(
            context,
            this
        )
    }

    private fun initActions() {

        presenter.startApp()

        bs_btn_close.setOnClickListener(this)
        bs_btn_show_list.setOnClickListener(this)
        mapfrag_btn_search.setOnClickListener(this)

        bottom_sheet_lv_cities.setOnItemClickListener { parent, view, position, id ->

            val city = parent.getItemAtPosition(position) as City

            presenter.getCityData(city)
        }
    }

    override fun onClick(view: View) = with(presenter) {

        when (view.id) {
            R.id.mapfrag_btn_search -> searchClicked()
            R.id.bs_btn_show_list -> openListClicked()
            R.id.bs_btn_close -> closeClicked()
        }
    }

    override fun showToastMessage(message: String) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun execNav(data: City) {

        val mIntent = Intent(context, CityInfoActivity::class.java)

        mIntent.putExtra("data", data)

        startActivity(mIntent)
    }

    override fun showProgressBar(state: Boolean) {

        mapfrag_progress_bar.isVisible = state
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        presenter.processPermissionResult(grantResults)
    }
}
