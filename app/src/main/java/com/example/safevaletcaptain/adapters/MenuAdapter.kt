package com.example.safevaletcaptain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.databinding.CellMenuItemBinding
import com.example.safevaletcaptain.helpers.HelperUtils


class MenuAdapter(val context: Context, val name:ArrayList<String>,
                  private val image:ArrayList<Int>, private val listener: OnMenuListener): RecyclerView.Adapter<MenuAdapter.Holder>() {
    private var navController: NavController? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            CellMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.binding.textView.text = name[position]
        holder.binding.imageView.setImageResource(image[position])
//        navController = Navigation.findNavController(holder.itemView)


        if (HelperUtils.getLang(context) == "ar"){
            holder.binding.arrowAr.setImageResource(R.drawable.rsz_1left)
        }else {
            holder.binding.arrowAr.setImageResource(R.drawable.rsz_righ)
        }


        holder.binding.textView.setOnClickListener{
            listener.onEvent(position)
        }

        holder.binding.allMenu.setOnClickListener{
            listener.onEvent(position)
        }
        holder.binding.carView.setOnClickListener{
            listener.onEvent(position)
        }
        holder.binding.linearView.setOnClickListener{
            listener.onEvent(position)
        }



        holder.binding.imageView.setOnClickListener{
            listener.onEvent(position)
        }

        holder.itemView.setOnClickListener{
            listener.onEvent(position)
        }

        holder.binding.imageView.setOnClickListener{
            listener.onEvent(position)
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }


    class Holder(val binding: CellMenuItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

}