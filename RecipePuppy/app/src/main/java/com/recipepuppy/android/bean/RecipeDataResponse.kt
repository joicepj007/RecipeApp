package com.recipepuppy.android.bean

import com.google.gson.annotations.SerializedName

data class RecipeDataResponse(

        @SerializedName("results") val results : List<ResultData>

)