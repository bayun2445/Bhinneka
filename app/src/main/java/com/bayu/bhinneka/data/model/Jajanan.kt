package com.bayu.bhinneka.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jajanan(
    val name: String,
    val description: String,
    val recipe: String,
    val nutrition: Nutrition,
) : Parcelable {
    constructor() : this(
        "",
        "", "",
        Nutrition()
    )
}
