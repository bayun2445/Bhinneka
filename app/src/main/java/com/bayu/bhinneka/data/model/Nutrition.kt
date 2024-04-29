package com.bayu.bhinneka.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrition(
    val carbs: Double,
    val calorie: Double,
    val fat: Double,
    val protein: Double,
    val natrium: Double,
    val kalium: Double,
) : Parcelable
