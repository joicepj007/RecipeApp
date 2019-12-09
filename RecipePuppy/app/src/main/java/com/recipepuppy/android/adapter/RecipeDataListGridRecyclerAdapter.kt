package com.recipepuppy.android.adapter

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recipepuppy.android.R
import com.recipepuppy.android.bean.ResultData
import kotlinx.android.synthetic.main.list_item_recipe.view.*


class RecipeDataListGridRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    private var listOfRecipe = listOf<ResultData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeDataListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_recipe,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listOfRecipe.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val recipeViewHolder = viewHolder as RecipeDataListViewHolder
        recipeViewHolder.bindView(listOfRecipe[position])
    }

    /**
     * Updates the list
     * */
    fun setRecipeDataList(listOfRecipes: List<ResultData>) {
        this.listOfRecipe = listOfRecipes
        notifyDataSetChanged()
    }


    inner class RecipeDataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(v: View?) {
            if (v != null) {
                mClickListener.onClick(adapterPosition, v)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun bindView(resultData: ResultData) {

            itemView.tvRecipeTitle.text = resultData.title
            Glide.with(itemView.context).load(resultData.thumbnail!!).into(itemView.imageMovie)
        }

    }

}