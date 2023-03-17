package com.hvasoft.stores.presentation.main_stores.adapter

import com.hvasoft.stores.domain.model.Store

interface OnClickListener {
    fun onClick(store: Store)
    fun onLongClick(store: Store)
    fun onFavoriteClick(store: Store)
}