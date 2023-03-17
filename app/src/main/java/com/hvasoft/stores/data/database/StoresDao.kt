package com.hvasoft.stores.data.database

import androidx.room.*
import com.hvasoft.stores.data.entities.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoresDao {

    @Query("SELECT * FROM StoreEntity")
    fun getAllStores() : Flow<List<StoreEntity>>

    @Query("SELECT * FROM StoreEntity where id = :id")
    suspend fun getStoreById(id: Long): StoreEntity?

    @Insert
    suspend fun insertStore(storeEntity: StoreEntity): Long

    @Update
    suspend fun updateStore(storeEntity: StoreEntity): Int

    @Delete
    suspend fun deleteStore(storeEntity: StoreEntity): Int

}