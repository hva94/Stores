package com.hvasoft.stores.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "StoreEntity", indices = [Index(value = ["name"], unique = true)])
data class StoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val phone: String,
    val website: String,
    val photoUrl: String,
    val isFavorite: Boolean
)
