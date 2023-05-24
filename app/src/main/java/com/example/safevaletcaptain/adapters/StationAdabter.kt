package com.example.safevaletcaptain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safevaletcaptain.databinding.CarItemsBinding
import com.example.safevaletcaptain.databinding.StationlistitemBinding
import com.example.safevaletcaptain.model.StationsName


class StationAdabter(private val list: List<StationsName>, val context: Context): RecyclerView.Adapter<StationAdabter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            StationlistitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
//holder.binding.stataionNamelist.text = list[position].name
//        holder.binding.stataionNamelist.text = list[position].id


    }

    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: StationlistitemBinding) : RecyclerView.ViewHolder(binding.root)

}