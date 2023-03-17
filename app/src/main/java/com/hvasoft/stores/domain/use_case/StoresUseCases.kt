package com.hvasoft.stores.domain.use_case

data class StoresUseCases(
    val getAllStores: GetAllStoresUseCase,
    val getSingleStore: GetSingleStoreUseCase,
    val insertStore: InsertStoreUseCase,
    val updateStore: UpdateStoreUseCase,
    val deleteStore: DeleteStoreUseCase
)