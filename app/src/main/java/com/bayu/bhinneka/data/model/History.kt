package com.bayu.bhinneka.data.model

data class History(
    var date: String? = null,
    var result: Jajanan? = null,
    var img: String? = null,
) {
    constructor(): this(
        "",
        Jajanan(),
        ""
    )
}
