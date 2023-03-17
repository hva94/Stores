package com.hvasoft.stores.domain.use_case

import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.repository.StoresRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllStoresUseCase(
    private val repository: StoresRepository
) {

    operator fun invoke(): Flow<List<Store>> {
        val storesFlow = repository.getAllStores()

        storesFlow.map { stores ->
            stores.sortedBy { it.name }
        }

        return storesFlow
    }

}