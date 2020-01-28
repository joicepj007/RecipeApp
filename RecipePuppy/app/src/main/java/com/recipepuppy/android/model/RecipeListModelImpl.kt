package com.recipepuppy.android.model

import com.recipepuppy.android.bean.RecipeDataResponse
import com.recipepuppy.android.network.ApiClient

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

open class RecipeListModelImpl {
    private var disposable: Disposable? = null

    fun filterData(query: String, callback: FilterResponseCallback?) :Disposable?{
        disposable = ApiClient.instance
            .getFilterRecipeResult(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ recipeDataResponseResponse ->
                val responseCode = recipeDataResponseResponse.code()
                if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
                    callback?.onFilterDataSuccess(recipeDataResponseResponse)
                } else {
                    callback?.onFilterDataError(Throwable("Something Went Wrong"))
                }
            }, { callback?.onFilterDataError(Throwable("Server Error")) }, { })
       return disposable
    }

    fun loadFullData(callback: MainResponseCallback):Disposable? {
        disposable = ApiClient.instance
            .loadUserData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ recipeDataResponseResponse ->
                val responseCode = recipeDataResponseResponse.code()
                if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
                    callback.onSuccess(recipeDataResponseResponse)
                } else {
                    callback.onError(Throwable("Something Went Wrong"))
                }
            }, { callback.onError(Throwable("Server Error")) }, { })
        return disposable
    }

    fun onStop() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    interface FilterResponseCallback {
        fun onFilterDataSuccess(response: Response<RecipeDataResponse>)

        fun onFilterDataError(error: Throwable)
    }

    interface MainResponseCallback {
        fun onSuccess(response: Response<RecipeDataResponse>)

        fun onError(error: Throwable)
    }
}
