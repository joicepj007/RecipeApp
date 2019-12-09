package com.recipepuppy.android.presenter

import com.recipepuppy.android.bean.RecipeDataResponse
import retrofit2.Response


interface ViewPresenter {

    interface MainView {

        fun onFilterDataSuccess(reposnseModel: Response<RecipeDataResponse>)
        fun onFilterDataError(throwable: Throwable)
    }

    interface LoadDataView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(reposnseModel: Response<RecipeDataResponse>)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun filterRecipeData(query: String)
        fun loadRecipeData()
        fun onStop()
    }
}