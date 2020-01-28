package com.recipepuppy.android.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recipepuppy.android.R
import com.recipepuppy.android.adapter.RecipeDataListGridRecyclerAdapter
import com.recipepuppy.android.bean.RecipeDataResponse
import com.recipepuppy.android.bean.ResultData
import com.recipepuppy.android.model.RecipeListModelImpl
import com.recipepuppy.android.network.ApiClient
import com.recipepuppy.android.network.ApiInterface
import com.recipepuppy.android.presenter.RecipeListPresenterImpl
import com.recipepuppy.android.presenter.ViewPresenter
import com.recipepuppy.android.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_searchview.*
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), ViewPresenter.MainView, ViewPresenter.LoadDataView,
    TextWatcher, View.OnTouchListener {


    lateinit var presenterImplementation: RecipeListPresenterImpl
    var mAndroidArrayList: ArrayList<ResultData>?=null
    lateinit var disposable: Disposable
    lateinit var movieListAdapter: RecipeDataListGridRecyclerAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        presenterImplementation = RecipeListPresenterImpl(this, this, RecipeListModelImpl())
        presenterImplementation.loadRecipeData()
        search_edt?.addTextChangedListener(this)
        search_edt?.setOnTouchListener(this)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /**
     * Called when user types on the search view
     * @param s text changed
     * @param count size of the text
     * */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        presenterImplementation.filterRecipeData(search_edt.text.toString())
        movieListAdapter.notifyDataSetChanged()
    }

    /**
     * Called on click of search icon
     * @param view search view
     * @param motionEvent action event
     * */
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (view) {
            search_edt -> {
                val DRAWABLE_RIGHT = 2
                when (motionEvent.getAction()) {
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        if (motionEvent.rawX >= search_edt.getRight() - search_edt.getCompoundDrawables().get(
                                DRAWABLE_RIGHT
                            ).getBounds().width()
                        ) {
                            presenterImplementation.filterRecipeData(search_edt.text.toString())
                            movieListAdapter.notifyDataSetChanged()
                            search_edt.hideKeyboard()
                            return true
                        }
                    }
                }
                return false
            }
        }
        return true
    }


    override fun showProgressbar() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        progressbar.visibility = View.GONE
    }

    /**
     * Callback for api success
     * @param reposnseModel Response object
     * */
    override fun onSuccess(reposnseModel: Response<RecipeDataResponse>) {
        if (reposnseModel.body() != null) {
            adapterInitialize(reposnseModel)
        }
    }

    /**
     * Callback to show api failure
     * */
    override fun onError(throwable: Throwable) {
        Log.d("onError", "" + throwable.printStackTrace())
    }

    /**
     * Callback to show success of api filter call
     * @param reposnseModel response object
     * */
    override fun onFilterDataSuccess(reposnseModel: Response<RecipeDataResponse>) {
        if (reposnseModel.body() != null) {
            adapterInitialize(reposnseModel)
        }
    }

    /**
     * Callback to show api failure
     * */
    override fun onFilterDataError(throwable: Throwable) {
        Log.d("onFilterDataError", "" + throwable.printStackTrace())
    }


    override fun onDestroy() {
        super.onDestroy()
        //to cancel the api call on app destroy,to solve memory leaks
        presenterImplementation.onStop()
    }

    private fun adapterInitialize(reposnseModel: Response<RecipeDataResponse>) {
        mAndroidArrayList = reposnseModel.body()?.results?.let { ArrayList(it) }
        rv_ingredients.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_ingredients?.itemAnimator = DefaultItemAnimator()
        movieListAdapter = RecipeDataListGridRecyclerAdapter()
        rv_ingredients.adapter = movieListAdapter
        mAndroidArrayList?.let { movieListAdapter.setRecipeDataList(it) }
        movieListAdapter.setOnItemClickListener(object :
            RecipeDataListGridRecyclerAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                val intent = Intent(this@MainActivity, RecipeDataDetailActivity::class.java)
                intent.putExtra(AppConstants.KEY_TITLE, mAndroidArrayList?.get(pos)?.title)
                intent.putExtra(AppConstants.KEY_THUMBNAIL, mAndroidArrayList?.get(pos)?.thumbnail)
                intent.putExtra(AppConstants.KEY_INGEDIENTS, mAndroidArrayList?.get(pos)?.ingredients)
                intent.putExtra(AppConstants.KEY_HREF, mAndroidArrayList?.get(pos)?.href)
                startActivity(intent)
            }
        })
    }

    /**
     * Called to hide the keyboard
     * */
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }

}
