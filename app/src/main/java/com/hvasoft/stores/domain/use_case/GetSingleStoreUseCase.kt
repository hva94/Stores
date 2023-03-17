package com.hvasoft.stores.domain.use_case

import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.repository.StoresRepository

class GetSingleStoreUseCase(
    private val repository: StoresRepository
) {

    suspend operator fun invoke(id: Long): Store? {
        return repository.getStoreById(id)
    }

}
