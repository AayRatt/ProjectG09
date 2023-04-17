package com.example.project_g09.networking

import com.example.project_g09.models.AllStatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //https://developer.nps.gov/api/v1/parks/?stateCode=CA
    // TODO: https://randomuser.me/api/?results=5
    // This will always return 5 results
//    @GET("?stateCode={id}&api_key=FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT")
//    suspend fun getStates(@Path("id")userID:String): Response<AllStatesResponse>

//    @GET("?stateCode={id}")
//    suspend fun getStates(
//        @Path("id") userID: String,
//        @Query("api_key") apiKey: String = "FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT"
//    ): Response<AllStatesResponse>

//    @GET("/")
//    suspend fun getStates(@Query("stateCode") stateCode: String, @Query("api_key") apiKey: String): Response<AllStatesResponse>

//    @GET("?stateCode={id}")
//    suspend fun getStates(
//        @Path("id") userID: String,
//        @Query("api_key") apiKey: String = "FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT"
//    ): Response<AllStatesResponse>

//    @GET("?stateCode={id}&api_key=FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT")
//    suspend fun getStates(@Path("id")userID:String): Response<AllStatesResponse>

    @GET("api/v1/parks/")
    suspend fun getStates(@Query("stateCode") stateCode: String, @Query("api_key") apiKey: String): Response<AllStatesResponse>


}