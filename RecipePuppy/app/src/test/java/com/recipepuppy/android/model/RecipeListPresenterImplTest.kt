package com.recipepuppy.android.model

import com.recipepuppy.android.bean.RecipeDataResponse
import com.recipepuppy.android.model.RecipeListModelImpl.FilterResponseCallback
import com.recipepuppy.android.network.ApiClient
import com.recipepuppy.android.presenter.RecipeListPresenterImpl
import com.recipepuppy.android.presenter.ViewPresenter.LoadDataView
import com.recipepuppy.android.presenter.ViewPresenter.MainView
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import retrofit2.Response

@RunWith(PowerMockRunner::class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(
    Scheduler::class,
    AndroidSchedulers::class,
    ApiClient::class
)
class RecipeListPresenterImplTest {
    @Mock
    lateinit var mainView: MainView
    @Mock
    lateinit var loadDataView: LoadDataView
    lateinit var recipeListPresenter: RecipeListPresenterImpl
    @Mock
    lateinit var recipeListModel: RecipeListModelImpl
    @Captor
    lateinit var filterResponseCallbackArgumentCaptor: ArgumentCaptor<FilterResponseCallback>
    @Captor
    lateinit var mainResponseCallbackArgumentCaptor: ArgumentCaptor<RecipeListModelImpl.MainResponseCallback>
    @Mock
    lateinit var callback: FilterResponseCallback
    lateinit var recipeDataResponse: Response<RecipeDataResponse>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        recipeListPresenter = RecipeListPresenterImpl(
            mainView,
            loadDataView,
            recipeListModel
        )
    }

    @Test
    fun filterRecipeDataSuccess() {
        recipeListPresenter.filterRecipeData("search")
        verify(
            recipeListModel
        ).filterData(
            eq("search"),
            filterResponseCallbackArgumentCaptor.capture()
        )
        filterResponseCallbackArgumentCaptor.value.onFilterDataSuccess(recipeDataResponse)
        verify(
            mainView
        ).onFilterDataSuccess(
            eq(
                recipeDataResponse
            )
        )
    }

    @Test
    fun filterRecipeDataFailure() {
        val error = Throwable("Something Went Wrong")
        recipeListPresenter.filterRecipeData("search")
        verify(
            recipeListModel
        ).filterData(
            eq("search"),
            filterResponseCallbackArgumentCaptor.capture()
        )
        filterResponseCallbackArgumentCaptor.value.onFilterDataError(error)
        verify(
            mainView
        ).onFilterDataError(eq(error))
    }

    @get:Test
    val fullDataSuccess: Unit
        get() {
            recipeListPresenter.loadRecipeData()
            verify(
                loadDataView
            ).showProgressbar()
            verify(
                recipeListModel
            ).loadFullData(mainResponseCallbackArgumentCaptor.capture())
            mainResponseCallbackArgumentCaptor.value.onSuccess(recipeDataResponse)
            verify(
                loadDataView
            ).hideProgressbar()
            verify(
                loadDataView
            ).onSuccess(
                eq(
                    recipeDataResponse
                )
            )
        }

    @get:Test
    val fullDataFailure: Unit
        get() {
            val error = Throwable("Something Went Wrong")
            recipeListPresenter.loadRecipeData()
            verify(
                loadDataView
            ).showProgressbar()
            verify(
                recipeListModel
            ).loadFullData(mainResponseCallbackArgumentCaptor.capture())
            mainResponseCallbackArgumentCaptor.value.onError(error)
            verify(
                loadDataView
            ).hideProgressbar()
            verify(
                loadDataView
            ).onError(eq(error))
        }
}