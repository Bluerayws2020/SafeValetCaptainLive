package com.example.safevaletcaptain.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.databinding.CarItemsBinding
import com.example.safevaletcaptain.fragments.PickingCarFragment
import com.example.safevaletcaptain.fragments.TripInProgressFragment
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.RideModel
import com.example.safevaletcaptain.viewmodel.RideViewModel

class RideAdabter(    private val onOrderListener: TakeOrder
                      ,
                      private val list: List<RideModel>, val context: Context): RecyclerView.Adapter<RideAdabter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            CarItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.binding.carModel.text = list[position].car_number
        holder.binding.keyNumber.text = list[position].key_number

        holder.binding.takeOrderBtn.setOnClickListener {
//                holder.binding.progressBarTake.show()
            onOrderListener.takeOrder( list[position].ride_id, list[position].key_number, list[position].car_number,holder.binding.progressBarTake)

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



    class Holder(val binding: CarItemsBinding) : RecyclerView.ViewHolder(binding.root)

}