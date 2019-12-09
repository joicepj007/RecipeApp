package com.recipepuppy.android.network

import android.view.SearchEvent
import com.recipepuppy.android.bean.RecipeDataResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("api/?q=")
    fun getRecipePupyData():Observable<Response<RecipeDataResponse>>

    @GET("api/")
    fun getFilterResult(@Query("q") action: String): Observable<Response<RecipeDataResponse>>


}