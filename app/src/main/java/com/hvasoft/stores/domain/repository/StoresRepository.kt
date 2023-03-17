package com.hvasoft.stores.domain.repository

import com.hvasoft.stores.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoresRepository {

    fun getAllStores(): Flow<List<Store>>

    suspend fun getStoreById(id: Long): Store?

    suspend fun insertStore(store: Store) : Long

    suspend fun updateStore(store: Store) : Int

    suspend fun deleteStore(store: Store) : Int

}