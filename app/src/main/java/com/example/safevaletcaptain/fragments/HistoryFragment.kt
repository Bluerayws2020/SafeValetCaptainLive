package com.example.safevaletcaptain.fragments

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.CarAdapter
import com.example.safevaletcaptain.adapters.HistoryAdapter
import com.example.safevaletcaptain.adapters.OpenGoogleMap
import com.example.safevaletcaptain.adapters.RideAdabter
import com.example.safevaletcaptain.databinding.FragmentHomeBinding
import com.example.safevaletcaptain.databinding.HistoryBinding
import com.example.safevaletcaptain.databinding.PickUpTheCarBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.model.RideHistoryData
import com.example.safevaletcaptain.model.RideModel
import com.example.safevaletcaptain.viewmodel.RideViewModel

class HistoryFragment: AppCompatActivity() {

    private var navController: NavController? = null
    private var historyAdapter: HistoryAdapter? = null
    private val rideViewModel by viewModels<RideViewModel>()

private lateinit var binding : HistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))
        //            setActionbarTitle(getString(R.string.Notfication))

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.History)


        binding.toolbarInclude.notficationBtn.setOnClickListener{



            startActivity(Intent(this,Notfication::class.java))

        }
        if (HelperUtils.getLang(this) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }
        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

        binding.toolbarInclude.menuNotfication.setOnClickListener{
            onBackPressed()
        }


        getHistoryData()



        binding.swipeToRefresh.setOnRefreshListener {
            binding.historyItems.adapter = null
            binding.progressBarStation.show()
            binding.noDatatxt.hide()
            rideViewModel.rideHistory()
        }

    }




    fun getHistoryData(){
        rideViewModel.rideHistory()


        rideViewModel.getRideHistory().observe(this) { result ->
            binding.swipeToRefresh.isRefreshing = false

            when (result) {
                is NetworkResults.Success -> {
if (result.data.msg.status == 1 ){


    setupRecyclerView(result.data.ride_history)
}else {
    binding.noDatatxt.show()
    binding.noDatatxt.text = result.data.msg.message
}
                    binding.progressBarStation.hide()

                    Toast.makeText(this,result.data.msg.message , Toast.LENGTH_SHORT).show()


                }
                is NetworkResults.Error -> {

                    result.exception.printStackTrace()
                }
            }
        }

    }


    //setup Recycler View
    fun setupRecyclerView(ride:List<RideHistoryData>){

        historyAdapter = HistoryAdapter(ride,this,object :OpenGoogleMap{
            override fun onEvent(position: Int, img: ImageView, lat: String, long: String) {



            }
        })
        val layoutManager = LinearLayoutManager(this)
        binding.historyItems.layoutManager = layoutManager
        binding.historyItems.adapter = historyAdapter

    }




}