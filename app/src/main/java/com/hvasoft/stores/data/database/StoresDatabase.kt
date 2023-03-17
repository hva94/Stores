package com.hvasoft.stores.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hvasoft.stores.data.entities.StoreEntity

@Database(entities = [StoreEntity::class], version = 1)
abstract class StoresDatabase : RoomDatabase() {

    abstract val storesDao: StoresDao

    companion object {
        const val DATABASE_NAME = "stores_db"
    }

}