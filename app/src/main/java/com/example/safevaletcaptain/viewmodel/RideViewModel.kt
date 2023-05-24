package com.example.safevaletcaptain.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.model.*
import com.example.safevaletcaptain.repo.NetworkRepository
import kotlinx.coroutines.launch
import java.io.File

class RideViewModel(application: Application): AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)
    private val repo = NetworkRepository
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val company_id = HelperUtils.getCompanyId(application.applicationContext)

    private val avaolibeRideLiveData = MutableLiveData<NetworkResults<MainRideResponse>>()

    private val startRideLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val driverCustomerInfoLiveData = MutableLiveData<NetworkResults<DriverResponseINFO>>()
    private val driverStatutsLiveData = MutableLiveData<NetworkResults<DriverResponseWorkOrNot>>()

    private val actionRideLiveData = MutableLiveData<NetworkResults<DriverResponseINFO>>()

    private val takeCarLiveData = MutableLiveData<NetworkResults<TakeCarMainModel>>()

    private val sendNotficationLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val startBackLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val endTripBackLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val sendDataLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()


    private val sendkeyLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()


    private val rideHistoryLivedata = MutableLiveData<NetworkResults<RideHistory>>()


    private val stationWorkLiveData = MutableLiveData<NetworkResults<StationResponse>>()

    private val driverStartWorkLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val notficationLiveData = MutableLiveData<NetworkResults<NotficationResponse>>()

    private val sendNotificationForAllLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()


    private val startForeignRideLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    fun getAvalibleRide(){
        viewModelScope.launch{
            avaolibeRideLiveData.value = repo.avalibleCarBack(uid,language)
        }
    }

    fun startRide(customer_id:String,lat:String,long:String){
        viewModelScope.launch{
            startRideLiveData.value = repo.startRide(language,uid,customer_id,lat,long)
        }
    }

    // Start Foreign Ride
    fun startForeignRide(ocr: String){
        viewModelScope.launch{
            startForeignRideLiveData.value = repo.startForeignRide(language,uid,ocr)
        }
    }


//---------Driver Statuts Api -------//

//    ride info
    fun getdriverCustomerInfo(){
        viewModelScope.launch{
            driverCustomerInfoLiveData.value = repo.driverStatutsCustomerInfo(uid,language)
        }
    }

    fun getdriverCustomerStatus(){
        viewModelScope.launch{
            driverStatutsLiveData.value = repo.driverStatutsCustomerWorkOrNot(uid,language)
        }
    }


    //    ride confirm

    fun getconfiremRide(){
        viewModelScope.launch{
            actionRideLiveData.value = repo.confirmRidInfo(uid,language,"1")
        }
    }


//    take Car Model

    fun getcarBack(ride_id:String){
        viewModelScope.launch{
            takeCarLiveData.value = repo.takeCarModel(language,uid,ride_id)
        }
    }

//    send Notficarion
    fun sendNotifcation(ride_id: String,type:String){
        viewModelScope.launch {

            sendNotficationLiveData.value = repo.sendNotfication(language,type,ride_id)
        }
    }
    fun startback(backStart:String){
        viewModelScope.launch {

            startBackLiveData.value = repo.startBack(language,uid,backStart)
        }
    }


    fun sendNotificationForAll( type: String){
        viewModelScope.launch{
            sendNotificationForAllLiveData.value = repo.getNotificationSendToAll(uid, type)
        }
    }


    fun endTripBackLiveData(delivered:String,backStart:String){
        viewModelScope.launch {

            endTripBackLiveData.value = repo.endBack(language,uid,backStart,delivered)
        }
    }


    fun sendKey(keyNumber:String,locationStr:String,){
        viewModelScope.launch {

            sendkeyLiveData.value = repo.sendKeyNumber(uid,language,keyNumber,locationStr)
        }
    }

//    sendDataLiveData,m

    fun sendDataToApi(
    profileImage: File,
    lat: String,
    lon: String,
    )

    {
        viewModelScope.launch {

            sendDataLiveData.value = repo.sendCarInfo(uid,language,profileImage,lat,lon,"00")
        }
    }

    fun rideHistory()

    {
        viewModelScope.launch {

            rideHistoryLivedata.value = repo.rideHistory(uid,language)
        }
    }



    fun stationWork()

    {
        viewModelScope.launch {

            stationWorkLiveData.value = repo.stationWork(company_id,language,uid)
        }
    }





    fun driverStartWork(station_id:String){

        viewModelScope.launch {

            driverStartWorkLiveData.value = repo.startWork(language,uid,station_id)
        }
    }

    fun getNotficationLive(){

        viewModelScope.launch {

            notficationLiveData.value = repo.getNotficaion (language,uid)
        }
    }


    //    rideHistoryLivedata
    fun getAvalibleRideResponse() = avaolibeRideLiveData

    fun startRideCall() = startRideLiveData

    fun driverWithCustomerInfo() = driverCustomerInfoLiveData

    fun getConfiremRideWithActiobn() = actionRideLiveData

    fun getTakeCarBack() = takeCarLiveData

    fun getNotficationtype() = sendNotficationLiveData

fun getStartBack() = startBackLiveData

    fun getEndTrip() = endTripBackLiveData


    fun sendCarInfo() = sendDataLiveData
    fun getKeyNumber() = sendkeyLiveData

    fun getRideHistory()  = rideHistoryLivedata


    fun getStationWork() = stationWorkLiveData

    fun driverStartWork () = driverStartWorkLiveData

    fun getNotfication () = notficationLiveData

    fun getDriverWorkOrNot () =  driverStatutsLiveData

    fun startForeignRideResponse() = startForeignRideLiveData

    fun sendNotificationForAllResponse() = sendNotificationForAllLiveData

}