package com.example.safevaletcaptain.adapters

import android.widget.ProgressBar

interface TakeOrder {
    fun takeOrder(ride_id: String, key_Number: String,car_number:String,pd:ProgressBar)

}