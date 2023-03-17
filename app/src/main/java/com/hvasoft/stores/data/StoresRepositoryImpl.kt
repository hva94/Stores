package com.hvasoft.stores.data

import com.hvasoft.stores.core.Utils.toDatabase
import com.hvasoft.stores.core.Utils.toDomain
import com.hvasoft.stores.data.database.StoresDao
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.repository.StoresRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoresRepositoryImpl(
    private val dao: StoresDao
) : StoresRepository {

    override fun getAllStores(): Flow<List<Store>> {
        val response = dao.getAllStores()
        return response.map { stores ->
            stores.map { store ->
                store.toDomain()
            }
        }
    }

    override suspend fun getStoreById(id: Long): Store? {
        return dao.getStoreById(id)?.toDomain()
    }

    override suspend fun insertStore(store: Store): Long {
        return dao.insertStore(store.toDatabase())
    }

    override suspend fun updateStore(store: Store): Int {
        return dao.updateStore(store.toDatabase())
    }

    override suspend fun deleteStore(store: Store): Int {
        return dao.deleteStore(store.toDatabase())
    }

}