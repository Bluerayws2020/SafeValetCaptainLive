package com.example.safevaletcaptain.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.HistoryAdapter
import com.example.safevaletcaptain.adapters.StationAdabter
import com.example.safevaletcaptain.databinding.ActivityHomeBinding
import com.example.safevaletcaptain.databinding.ActivityStationListBinding
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.model.RideHistoryData
import com.example.safevaletcaptain.model.StationsName
import com.example.safevaletcaptain.viewmodel.RideViewModel

class StationList : AppCompatActivity() {
    private var navController: NavController? = null
    private var statAdapter: StationAdabter? = null
    private val rideViewModel by viewModels<RideViewModel>()

    private lateinit var binding: ActivityStationListBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityStationListBinding.inflate(layoutInflater)
        setContentView(binding.root)







    }





}