package com.example.safevaletcaptain.fragments

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.NotficationAdabter
import com.example.safevaletcaptain.databinding.*
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.model.NotficationDataResponse
import com.example.safevaletcaptain.viewmodel.RideViewModel

    class Notfication: AppCompatActivity(){
        init {
            d("ayham","Notfication")
        }

        private var navController: NavController? = null
        private var notficationAdabter: NotficationAdabter? = null
    private val rideViewModel by viewModels<RideViewModel>()



        private lateinit var binding : ActivityNotficationBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityNotficationBinding.inflate(layoutInflater)
            setContentView(binding.root)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))


            binding.toolbarInclude.toolbar.title = resources.getString(R.string.Notfication)


            binding.toolbarInclude.notficationBtn.setOnClickListener{



//                startActivity(Intent(this,Notfication::class.java))

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


        rideViewModel.getNotficationLive()


//
//        call api
                rideViewModel.getNotfication().observe(this) { result ->
                    binding.swipeToRefresh.isRefreshing = false
                    when (result) {
                        is NetworkResults.Success -> {
                            if (result.data.msg.status  == 1) {

                                setupRecyclerView(result.data.notficationData)
                                binding.progressBarStation.hide()
//
                                if(result.data.notficationData.isNullOrEmpty()){
                                    binding.noDatatxt.show()

                                }

                            }

                        }
                        is NetworkResults.Error -> {
                            result.exception.printStackTrace()
                        }
                    }
                }



        binding.swipeToRefresh.setOnRefreshListener {
            binding.notficaionItems.adapter = null
            binding.progressBarStation.show()
            binding.noDatatxt.hide()
            rideViewModel.getNotficationLive()
        }


    }




    //setup Recycler View
    fun setupRecyclerView(ride: ArrayList<ArrayList<NotficationDataResponse>>){

        notficationAdabter = NotficationAdabter(ride,this)
        val layoutManager = LinearLayoutManager(this)
        binding.notficaionItems.layoutManager = layoutManager
        binding.notficaionItems.adapter = notficationAdabter

    }


}