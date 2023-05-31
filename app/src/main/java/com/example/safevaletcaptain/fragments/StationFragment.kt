package com.example.safevaletcaptain.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log.d
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.CarAdapter
import com.example.safevaletcaptain.databinding.PickUpTheCarBinding
import com.example.safevaletcaptain.databinding.StationBinding
import com.example.safevaletcaptain.helpers.HelperUtils

class StationFragment: BaseFragment<StationBinding>() {

    init {
        d("ayham","StationFragment")
    }

    private var navController: NavController? = null
    private var carAdapter: CarAdapter? = null
    private val company_id = HelperUtils.getCompanyId(context)
    var mpopup: PopupWindow? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): StationBinding {
        return StationBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
binding.stataionName.setOnClickListener{
stationPopUp()
}




    }


    fun stationPopUp() {
        val popUpView: View = layoutInflater.inflate(
            R.layout.activity_station_list,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT, true
        ) // Creation of popup

        mpopup!!.setAnimationStyle(android.R.style.Animation_Dialog)
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
        val disimisView = popUpView.findViewById<View>(R.id.disimisView) as ConstraintLayout


//        disimisView.setOnClickListener {
//            mpopup!!.dismiss()
////    call Driver Status api with Action
//
//
//        }
    }




}