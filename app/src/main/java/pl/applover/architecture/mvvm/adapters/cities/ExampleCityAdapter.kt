package pl.applover.architecture.mvvm.adapters.cities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.applover.architecture.mvvm.R
import pl.applover.architecture.mvvm.databinding.ExampleItemCityBinding
import pl.applover.architecture.mvvm.models.example.ExampleCityModel

/**
 * Created by Janusz Hain on 2018-06-20.
 */

/**
 * Adapter without pagining
 */
class ExampleCityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cities: ArrayList<ExampleCityModel> = ArrayList()

    fun replaceCities(cities: Collection<ExampleCityModel>) {
        this.cities.clear()
        this.cities.addAll(cities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ExampleItemCityBinding = ExampleItemCityBinding.inflate(inflater, parent, false)
        return ExampleCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExampleCityViewHolder).bind(cities[position])
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.example_item_city
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}