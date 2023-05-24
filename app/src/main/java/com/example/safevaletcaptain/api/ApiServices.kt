package com.example.safevaletcaptain.api

import com.example.safevaletcaptain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServices {

    @Multipart
    @POST("Driverlogin/")
    suspend fun driverLogin(
        @Part("phone") phone: RequestBody,
        @Part("device_player_id") device_player_id: RequestBody,

    ): MessageModelRespose

    @Multipart
    @POST("Driverlogin/")
    suspend fun driverLoginWithOtp(
        @Part("phone") phone: RequestBody,
        @Part("otp") otp: RequestBody,
        @Part("device_player_id") device_player_id: RequestBody,
        @Part("lang") lang: RequestBody,

        ): LoginDriverModel


    @Multipart
    @POST("reSendOtp/")
    suspend fun resendOtp(
        @Part("phone") phone: RequestBody,
        @Part("user_kind") user_kind: RequestBody,

        ): MessageModelRespose
    @Multipart
    @POST("getAvailableCarBack/")
    suspend fun availableCarBack(
        @Part("uid") uid: RequestBody,
        @Part("lang") lang: RequestBody,

        ): MainRideResponse


    @GET("StartRide")
    suspend fun startRide(
        @Query("lang") lang: String,
        @Query("driver_id") driver_id: String,
        @Query("customer_id") customer_id: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String,

        ): MessageModelRespose

    @Multipart
    @POST("DriverStatus")
    suspend fun driverStatusInfo(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,

        ): DriverResponseINFO

    @Multipart
    @POST("DriverStatus")
    suspend fun driverStatusWork(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,

        ): DriverResponseWorkOrNot


    @Multipart
    @POST("DriverStatus")
    suspend fun confiremRide(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("action") action: RequestBody,

        ): DriverResponseINFO

    @Multipart
    @POST("DriverStatus")
    suspend fun driverSendCarInfo(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part profileImage: MultipartBody.Part?,
        @Part("image") imageName: RequestBody?,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("OCR") OCR: RequestBody,

        ): MessageModelRespose



    @Multipart
    @POST("DriverStatus")
    suspend fun sendKeyNumber(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("key_no") key_no: RequestBody,
        @Part("location_str") location_str: RequestBody,


        ): MessageModelRespose



    @Multipart
    @POST("takeCarBack")
    suspend fun takeCarBack(
        @Part("lang") lang: RequestBody,
        @Part("driver_id") driver_id: RequestBody,
        @Part("ride_id") ride_id: RequestBody,

        ): TakeCarMainModel

    @Multipart
    @POST("notificationTypeToSend")
    suspend fun notificationTypeToSend(
        @Part("lang") lang: RequestBody,
        @Part("type") type: RequestBody,
        @Part("ride_id") ride_id: RequestBody,

        ): MessageModelRespose

    @Multipart
    @POST("DriverStatus")
    suspend fun driverStatusStartBack(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("backStart") backStart: RequestBody,

        ): MessageModelRespose

    @Multipart
    @POST("DriverStatus")
    suspend fun driverStatusendBack(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("backStart") backStart: RequestBody,
        @Part("delivered") delivered: RequestBody,

        ): MessageModelRespose



    @Multipart
    @POST("ridesHistoryForCaptain")
    suspend fun getRideHistory(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,


        ): RideHistory


    @Multipart
    @POST("getStationsByCompany")
    suspend fun getStationsByCompany(
        @Part("lang") lang: RequestBody,
        @Part("company_id") company_id: RequestBody,

        @Part("uid") uid: RequestBody,

        ): StationResponse



    @Multipart
    @POST("driverActiveAndStation")
    suspend fun driverStartWork(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("station_id") station_id: RequestBody,



        ): MessageModelRespose

    @Multipart
    @POST("getNotficaition")
    suspend fun getNotficaition(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,




        ): NotficationResponse

    @Multipart
    @POST("logout")
    suspend fun getCustomerLogout(
        @Part("uid") uid: RequestBody

    ): MessageModelRespose

    @GET("addSecondaryRide")
    suspend fun startForeignRide(
        @Query("lang") lang: String,
        @Query("driver_id") driver_id: String,
        @Query("ocr") ocr: String

    ): MessageModelRespose


    @Multipart
    @POST("notificationTypeToSend/")
    suspend fun getNotificationTypeToSend(
        @Part("uid") uid: RequestBody,
        @Part("type") type: RequestBody

    ): MessageModelRespose




}