package com.hvasoft.stores.domain.utils

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.hvasoft.stores.R

object Validations {

    fun Fragment.validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true

        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty()) {
                textField.error = getString(R.string.helper_required)
                isValid = false
            } else textField.error = null
        }

        if (!isValid) view?.let {
            Snackbar.make(it, R.string.edit_store_message_valid, Snackbar.LENGTH_SHORT)
                .show()
        }

        return isValid
    }

}