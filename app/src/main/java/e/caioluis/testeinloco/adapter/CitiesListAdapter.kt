package e.caioluis.testeinloco.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import e.caioluis.testeinloco.R
import e.caioluis.testeinloco.json.City

class CitiesListAdapter(
    context: Context,
    private var resource: Int,
    private var data: ArrayList<City>

) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val mView: View? = mInflater.inflate(resource, parent, false)

        val tv_city = mView?.findViewById<TextView>(R.id.mapfrag_list_item)

        val item = data[position]

        tv_city?.text = item.name

        return mView!!
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {

        return data[position].id.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}