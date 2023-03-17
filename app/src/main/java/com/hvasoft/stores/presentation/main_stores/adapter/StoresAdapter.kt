package com.hvasoft.stores.presentation.main_stores.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hvasoft.stores.R
import com.hvasoft.stores.core.Utils.loadImage
import com.hvasoft.stores.databinding.ItemStoreBinding
import com.hvasoft.stores.domain.model.Store

class StoresAdapter(private val listener: OnClickListener) :
    ListAdapter<Store, RecyclerView.ViewHolder>(StoreDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val store = getItem(position)

        with(holder as ViewHolder) {
            setListener(store)

            binding.tvName.text = store.name
            binding.cbFavorite.isChecked = store.isFavorite
            binding.imgPhoto.loadImage(store.photoUrl)
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)

        fun setListener(store: Store) {
            with(binding) {
                root.setOnClickListener { listener.onClick(store) }

                root.setOnLongClickListener {
                    listener.onLongClick(store)
                    true
                }

                cbFavorite.setOnClickListener {
                    listener.onFavoriteClick(store)
                }
            }
        }
    }

    class StoreDiffCallback : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem == newItem
        }
    }

}