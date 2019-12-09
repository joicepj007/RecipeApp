package com.recipepuppy.android.presenter

import com.recipepuppy.android.bean.RecipeDataResponse
import com.recipepuppy.android.model.RecipeListModelImpl

import retrofit2.Response

class RecipeListPresenterImpl(
    private val mainView: ViewPresenter.MainView,
    private val loadDataView: ViewPresenter.LoadDataView,
    private val recipeListModel: RecipeListModelImpl
) {

    fun filterRecipeData(query: String) {
        recipeListModel.filterData(query, object : RecipeListModelImpl.FilterResponseCallback {
            override fun onFilterDataSuccess(response: Response<RecipeDataResponse>) {
                mainView.onFilterDataSuccess(response)
            }

            override fun onFilterDataError(error: Throwable) {
                mainView.onFilterDataError(error)
            }
        })
    }

    fun loadRecipeData() {
        loadDataView.showProgressbar()
        recipeListModel.loadFullData(object : RecipeListModelImpl.MainResponseCallback {
            override fun onSuccess(response: Response<RecipeDataResponse>) {
                loadDataView.hideProgressbar()
                loadDataView.onSuccess(response)
            }

            override fun onError(error: Throwable) {
                loadDataView.hideProgressbar()
                loadDataView.onError(error)
            }
        })
    }

    fun onStop() {
        recipeListModel.onStop()
    }
}
