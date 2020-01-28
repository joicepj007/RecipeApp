package com.recipepuppy.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recipepuppy.android.R
import java.util.ArrayList


class IngredientsListRecyclerAdapter(val mContext: Context ,val subList :Array<String>) : RecyclerView.Adapter<IngredientsListRecyclerAdapter.SubViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //return appropriate view holder

        return SubViewHolder(layoutInflater.inflate(R.layout.rec_view_ingredient_item, parent, false))

    }
    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val recipelist: String = subList.get(position)
        holder.subNameTv.text = recipelist

    }

    override fun getItemCount() = subList.size


    class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subNameTv: TextView = view.findViewById(R.id.sub_name_tv)

    }



}
