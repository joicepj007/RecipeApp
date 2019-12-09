package com.recipepuppy.android.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.recipepuppy.android.R
import com.recipepuppy.android.adapter.IngredientsListRecyclerAdapter
import com.recipepuppy.android.util.RecViewItemDecoration
import com.recipepuppy.android.utils.AppConstants
import kotlinx.android.synthetic.main.activity_main.rv_ingredients
import kotlinx.android.synthetic.main.activity_recipe_data_detail.*


class RecipeDataDetailActivity : AppCompatActivity() {
    //adaptor to show list of subscribers
    lateinit var mAdapter: IngredientsListRecyclerAdapter

    //adaptor to show list of subscribers
    lateinit var mhref:String

    //layout manager to show vertical list
    lateinit var mLinearLayoutManager: LinearLayoutManager

    //sets margin to top of the first item
    lateinit var recViewItemDecoration: RecViewItemDecoration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recipe_data_detail)

        initialize();

        rv_ingredients.layoutManager = mLinearLayoutManager

        rv_ingredients.addItemDecoration(recViewItemDecoration)

        rv_ingredients.adapter = mAdapter

    }

    private fun initialize()
    {
        val title=intent.getStringExtra(AppConstants.KEY_TITLE)
        val thumbnail=intent.getStringExtra(AppConstants.KEY_THUMBNAIL)
        mhref=intent.getStringExtra(AppConstants.KEY_HREF)
        val ingredients=intent.getStringExtra(AppConstants.KEY_INGEDIENTS)
        val ingredientsList = ingredients.split(",").toTypedArray()
        mAdapter= IngredientsListRecyclerAdapter(this, ingredientsList)
        mLinearLayoutManager= LinearLayoutManager(this)
        recViewItemDecoration=RecViewItemDecoration(this.resources.getDimensionPixelSize(R.dimen.ingredient_rv_item_space))
        tvRecipeTitle.text=title
        Glide.with(this).load(thumbnail!!).into(iv_recipe)
        tv_webview_click.setOnClickListener {
            val intent = Intent(this@RecipeDataDetailActivity,WebViewActivity::class.java)
            intent.putExtra("href", mhref)
            startActivity(intent)
        }

        bt_back.setOnClickListener {
            finish()

        }

    }

    override fun onBackPressed() {
        finish()
    }
}