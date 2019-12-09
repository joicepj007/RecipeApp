package com.recipepuppy.android.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by him on 2/11/2018.
 */
//kotlin data class generated from json response

data class ResultData(

        @SerializedName("title") val title: String = "",
        @SerializedName("thumbnail") val thumbnail: String = "",
        @SerializedName("ingredients") val ingredients: String = "",
        @SerializedName("href") val href: String = ""

)