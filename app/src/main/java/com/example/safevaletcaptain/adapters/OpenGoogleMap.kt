package com.example.safevaletcaptain.adapters

import android.widget.ImageView

interface OpenGoogleMap {

    fun onEvent(position: Int,img:ImageView,lat:String,long:String)

}