package com.bayu.bhinneka.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val id: String,
    val timeStamp: Long,
    val resultJajananName: String,
    val imgPath: String?,
    val matchScore: Float?,
) : Parcelable {
    constructor() : this(
        "",
        0,
        "",
        "",
        0f,
    )
}
