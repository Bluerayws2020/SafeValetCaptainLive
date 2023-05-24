package com.example.safevaletcaptain.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safevaletcaptain.databinding.CarItemsBinding
import com.example.safevaletcaptain.databinding.StationBinding
import com.example.safevaletcaptain.databinding.StationlistitemBinding
import com.example.safevaletcaptain.fragments.PickingCarFragment
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.model.NotficationDataResponse
import com.example.safevaletcaptain.model.NotficationResponse
import com.example.safevaletcaptain.model.RideModel






class NotficationAdabter(private val list: ArrayList<ArrayList<NotficationDataResponse>> , val context: Context): RecyclerView.Adapter<NotficationAdabter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            StationlistitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


        if(HelperUtils.getLang(context) == "ar" ){
            holder.binding.statTittle.text = list[position][0].bodyAR

        }else {
            holder.binding.statTittle.text = list[position][0].bodyEN

        }

       var date = list[position][0].date

        var time = list[position][0].time

        holder.binding.stationDate .text = "$date \t $time"


    }

    override fun getItemCount(): Int {
        return list.size
    }



    class Holder(val binding: StationlistitemBinding) : RecyclerView.ViewHolder(binding.root)

}
