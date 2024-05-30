package com.bayu.bhinneka.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrition(
    var carbs: Double? = null,
    var calorie: Double? = null,
    var fat: Double? = null,
    var protein: Double? = null,
    var natrium: Double? = null,
    var kalium: Double? = null,
) : Parcelable
