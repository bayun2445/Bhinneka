package com.bayu.bhinneka.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.bayu.bhinneka.R
import com.google.android.material.textfield.TextInputEditText

class CustomEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        doOnTextChanged { text, _, _, _ ->
            validateInputText(text.toString())
        }
    }

    private fun validateInputText(text: String) {
        if (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD > 0) {
            if (text.length < 8) {
                error = context.getString(R.string.error_password)
            }
        } else if (inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS > 0) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                error = context.getString(R.string.error_email)
            }
        }
    }
}