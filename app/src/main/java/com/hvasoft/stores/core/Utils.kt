package com.hvasoft.stores.core

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.hvasoft.stores.R
import com.hvasoft.stores.data.entities.StoreEntity
import com.hvasoft.stores.domain.model.Store

object Utils {

    fun StoreEntity.toDomain() = Store(
        id = id,
        name = name,
        phone = phone,
        website = website,
        photoUrl = photoUrl,
        isFavorite = isFavorite
    )

    fun Store.toDatabase() = StoreEntity(
        id = id,
        name = name,
        phone = phone,
        website = website,
        photoUrl = photoUrl,
        isFavorite = isFavorite
    )

    fun Fragment.showMessage(msgRes: Int, isError: Boolean = false) {
        val duration = if (isError) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        view?.let { Snackbar.make(it, msgRes, duration).show() }
    }

    fun Fragment.hideKeyboard() {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Fragment.startIntent(intent: Intent) {
        activity?.let {
            if (intent.resolveActivity(it.packageManager) != null)
                startActivity(intent)
            else
                showMessage(R.string.main_error_no_resolve, true)
        }
    }

    fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun ImageView.loadImage(url: String?) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(this)
    }
}