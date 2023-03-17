package com.hvasoft.stores.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.stores.core.exceptions.StoresException
import com.hvasoft.stores.core.exceptions.TypeError
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.repository.StoresRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertStoreUseCase(
    private val repository: StoresRepository
) {

    @Throws(StoresException::class)
    suspend operator fun invoke(store: Store) = withContext(Dispatchers.IO) {
        if (store.name.isBlank()) {
            throw StoresException(TypeError.EMPTY_NAME)
        }
        if (store.phone.isBlank()) {
            throw StoresException(TypeError.EMPTY_PHONE)
        }
        if (store.photoUrl.isBlank()) {
            throw StoresException(TypeError.EMPTY_PHOTO_URL)
        }

        try {
            repository.insertStore(store)
        } catch (e: SQLiteConstraintException) {
            throw StoresException(TypeError.INSERT)
        }
    }

}