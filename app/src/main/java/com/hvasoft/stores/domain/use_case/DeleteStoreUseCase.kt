package com.hvasoft.stores.domain.use_case

import com.hvasoft.stores.core.exceptions.StoresException
import com.hvasoft.stores.core.exceptions.TypeError
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.repository.StoresRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteStoreUseCase(
    private val repository: StoresRepository
) {
    suspend operator fun invoke(store: Store) = withContext(Dispatchers.IO) {
        val result = repository.deleteStore(store)
        if (result == 0) throw StoresException(TypeError.DELETE)
    }
}
