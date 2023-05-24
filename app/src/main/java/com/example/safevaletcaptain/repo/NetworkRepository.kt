package com.example.safevaletcaptain.repo

import android.util.Log
import com.example.safevaletcaptain.api.ApiClient
import com.example.safevaletcaptain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File
import java.lang.Exception

object NetworkRepository {

    suspend fun driverLogin(
    phone: String,
    device_player_id: String
    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val devicePlayerIdBody = device_player_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.driverLogin(
                    phoneBody,
                    devicePlayerIdBody
                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun driverLoginWithOtp(
        phone: String,
        otp:String,
        device_player_id: String,
        lang: String

    ): NetworkResults<LoginDriverModel> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val devicePlayerIdBody = device_player_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val otpBody = otp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.driverLoginWithOtp(
                    phoneBody,
                    otpBody,
                    devicePlayerIdBody,
                    langBody

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun resendOtp(
        phone: String,
        userKind:String,

    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userKindBody = userKind.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.resendOtp(
                    phoneBody,
                    userKindBody,

                    )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun avalibleCarBack(
        uid: String,
        lang: String,
    ): NetworkResults<MainRideResponse> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.availableCarBack(
                    uidBody,
                    langBody
                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }





//    start Ride
suspend fun startRide(
    lang: String,
    driver_id: String,

    customer_id: String,
    lat: String,
    lon: String,



    ): NetworkResults<MessageModelRespose> {
    return withContext(Dispatchers.IO){


        try {
            val results = ApiClient.retrofitService.startRide(
                lang,
                driver_id,
                customer_id,
                lat,
                lon
            )
            NetworkResults.Success(results)
        } catch (e: java.lang.Exception){
            NetworkResults.Error(e)
        }
    }
}




    //   DriverStatuts With Customer Statuts
    suspend fun driverStatutsCustomerInfo(
        uid: String,

        lang: String,



        ): NetworkResults<DriverResponseINFO> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.driverStatusInfo(
                    langBody,
                    uidBody,

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun driverStatutsCustomerWorkOrNot(
        uid: String,

        lang: String,



        ): NetworkResults<DriverResponseWorkOrNot> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.driverStatusWork(
                    langBody,
                    uidBody,

                    )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun confirmRidInfo(
        uid: String,

        lang: String,
        action: String,



        ): NetworkResults<DriverResponseINFO> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val actionBody = action.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.confiremRide(
                    langBody,
                    uidBody,
                    actionBody

                    )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }



//    @Part("lang") lang: RequestBody,
//    @Part("uid") uid: RequestBody,
//    @Part profileImage: MultipartBody.Part?,
//    @Part("image") imageName: RequestBody?,
//    @Part("lat") lat: RequestBody,
//    @Part("lon") lon: RequestBody,
//    @Part("OCR") OCR: RequestBody,


    //    send clint Info
suspend fun sendCarInfo(
        uid: String,
        lang: String,
        profileImage: File,
        lat: String,
        lon: String,
        ocr: String,



        ): NetworkResults<MessageModelRespose> {
    return withContext(Dispatchers.IO){
        Log.d("IMAGE DONE ","DOne")

        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val latBody = lat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val lonBody = lon.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ocrBody = ocr.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        try {

            var imagePart: MultipartBody.Part? = null
            profileImage?.let {
                imagePart = MultipartBody.Part.createFormData(
                    "image",
                    it.name,
                    it.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }


            val results = ApiClient.retrofitService.driverSendCarInfo(
                langBody,
                uidBody,
                imagePart,
                profileImage?.name?.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                latBody,
                lonBody,
                ocrBody

            )
            NetworkResults.Success(results)
        } catch (e: java.lang.Exception){
            NetworkResults.Error(e)
        }
    }
}


    //    send car Key Number
    suspend fun sendKeyNumber(
        uid: String,
        lang: String,
        key_number:String
,        locationStr:String



    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val keyNumberBody = key_number.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val locationStrBody = locationStr.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {




                val results = ApiClient.retrofitService.sendKeyNumber(
                    langBody,
                    uidBody,
                    keyNumberBody,
                    locationStrBody


                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }


//    take car

    suspend fun takeCarModel(
        lang: String,

        uid: String,

        ride_id: String,



        ): NetworkResults<TakeCarMainModel> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val rideIdBody = ride_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.takeCarBack(
                    langBody,
                    uidBody,
                    rideIdBody

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }






    //    mesage respose
    suspend fun sendNotfication(
        lang: String,
        type: String,
        ride_id:String



        ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val rideIdBody = ride_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.notificationTypeToSend(
                    langBody,
//                    uidBody,
                    typeBody,
                    rideIdBody

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }

    //    mesage respose
    suspend fun startBack(
        lang: String,
        uid:String,

        backStart:String



    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val backStartBody = backStart.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.driverStatusStartBack(
                    langBody,
                    uidBody,
                    backStartBody

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }



    //    mesage respose
    suspend fun endBack(
        lang: String,
        uid:String,

        backStart:String,
        delivered:String,



        ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val backStartBody = backStart.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val deliveredBody = delivered.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.driverStatusendBack(
                    langBody,
                    uidBody,
                    backStartBody,
                    deliveredBody

                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }






//    Ride History
//    mesage respose
suspend fun rideHistory(
    uid:String,
    lang: String,





    ): NetworkResults<RideHistory> {
    return withContext(Dispatchers.IO){
        val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        try {
            val results = ApiClient.retrofitService.getRideHistory(
                langBody,
                uidBody,


            )
            NetworkResults.Success(results)
        } catch (e: java.lang.Exception){
            Log.d("History ERR",e.localizedMessage.toString())
            NetworkResults.Error(e)
        }
    }
}






//station Name
    suspend fun stationWork(
    company_id:String,
        lang: String,


    uid: String,



        ): NetworkResults<StationResponse> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val company_idBody = company_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getStationsByCompany(
                    langBody,
                    company_idBody,
uidBody

                    )



                Log.d("ComapnnyID",company_id)
                Log.d("UIIID",uid)

                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                Log.d("ERRRRA",e.localizedMessage.toString())
                NetworkResults.Error(e)
            }
        }
    }




    //start Work Driver
    suspend fun startWork(
        lang: String,
        uid:String,

        station_id:String,





        ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val station_idBody = station_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            try {
                val results = ApiClient.retrofitService.driverStartWork(
                    langBody,
                    uidBody,
                    station_idBody


                    )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }



    //start Work Driver
    suspend fun getNotficaion(
        lang: String,
        uid:String,






        ): NetworkResults<NotficationResponse> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            try {
                val results = ApiClient.retrofitService.getNotficaition(
                    langBody,
                    uidBody,


                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun logoutUser(
        uid: String
    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerLogout(
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    //    start Foreign Ride
    suspend fun startForeignRide(
        lang: String,
        driver_id: String,
        ocr: String


    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){


            try {
                val results = ApiClient.retrofitService.startForeignRide(
                    lang,
                    driver_id,
                    ocr
                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getNotificationSendToAll(
        uid: String,
        type: String

    ): NetworkResults<MessageModelRespose> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getNotificationTypeToSend(
                    uidBody,
                    typeBody
                )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }


}