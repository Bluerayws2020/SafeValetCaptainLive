package com.example.safevaletcaptain.adapters

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.databinding.HistoryItemsBinding
import com.example.safevaletcaptain.model.RideHistoryData


class HistoryAdapter(private val list: List<RideHistoryData>, val context: Context
,private  val  openGoogleMap: OpenGoogleMap
): RecyclerView.Adapter<HistoryAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            HistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
//set data from model
        val data = list[position]
        val rootView = holder.binding

        rootView.date.text = data.date
        rootView.carName.text = data.carName
        rootView.carName2.text = data.carName
        rootView.carNumber.text = data.carID
        rootView.carNumber2.text = data.carID
        rootView.typeStatus.text = data.type
        rootView.startTime.text = data.time1
        rootView.endtime.text = data.time2
        rootView.driverName.text = data.driver1

rootView.tottalTime.text = context.getString(R.string.tottalTime,data.totalTime.toString())

        Log.d("LLLLOOOOOG",data.totalTime.toString())

//        click










        rootView.dropStat.setOnClickListener{
val lat = list[position].drop?.lat
            val long = list[position].drop?.lon

            openGoogleMap.onEvent(position,rootView.dropEnd,lat.toString(),long.toString())



        }

        rootView.dropEnd.setOnClickListener{
            val lat = list[position].drop?.lat
            val long = list[position].drop?.lon
            openGoogleMap.onEvent(position,rootView.dropEnd,lat.toString(),long.toString())





        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: HistoryItemsBinding) : RecyclerView.ViewHolder(binding.root)

    //open url in app
    @Throws(PackageManager.NameNotFoundException::class)
    fun openApp(url: String, mPackage: String, alternativeUrl: String = url) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(mPackage)
        val applicationInfo: ApplicationInfo =
            context.packageManager.getApplicationInfo(mPackage, 0)
        if (applicationInfo.enabled) {
            when {
                intent.resolveActivity(context.packageManager) != null -> context.startActivity(
                    intent
                )
                else -> openBrowser(alternativeUrl)
            }
        }
    }


    fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(context.packageManager) != null) context.startActivity(
            browserIntent
        )

//            showMessage(context.getString(R.string.error_occurred))
    }

}