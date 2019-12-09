package com.recipepuppy.android.network

import com.google.gson.GsonBuilder
import com.recipepuppy.android.bean.RecipeDataResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

open class ApiClient {

    private val myAppService: ApiInterface

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        myAppService = retrofit.create(ApiInterface::class.java)
    }

    companion object {
        private val BASE_URL = "http://www.recipepuppy.com/"
        private var apiClient: ApiClient? = null
        /**
         * Gets my app client.
         *
         * @return the my app client
         */
        val instance: ApiClient
            get() {
                if (apiClient == null) {
                    apiClient = ApiClient()
                }
                return apiClient as ApiClient
            }
    }

    /**
     *
     * @return the list of recipe data
     */
    fun loadUserData(): Observable<Response<RecipeDataResponse>> {

        return myAppService.getRecipePupyData()

    }

    /**
     *
     * @return the list of filtered recipe data
     */
    fun getFilterRecipeResult(query: String): Observable<Response<RecipeDataResponse>> {
        return myAppService.getFilterResult(query)
    }


}