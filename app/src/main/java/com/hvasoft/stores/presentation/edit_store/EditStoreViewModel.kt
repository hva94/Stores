package com.hvasoft.stores.presentation.edit_store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hvasoft.stores.core.exceptions.StoresException
import com.hvasoft.stores.core.exceptions.TypeError
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.use_case.StoresUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStoreViewModel @Inject constructor(
    private val storesUseCases: StoresUseCases
) : ViewModel() {

    private var _stateFlow = MutableStateFlow<Store?>(null)
    val stateFlow: StateFlow<Store?> get() = _stateFlow

    private var _typeError = MutableStateFlow(TypeError.NONE)
    val typeError: StateFlow<TypeError> get() = _typeError

    fun getStoreSelected(store: Store): StateFlow<Store?> {
        val storeFlow = MutableStateFlow<Store?>(null)
        viewModelScope.launch(Dispatchers.IO) {
            storeFlow.value = storesUseCases.getSingleStore(store.id)
        }
        return storeFlow
    }

    fun insertStore(store: Store) {
        executeAction(store) { storesUseCases.insertStore(store) }
    }

    fun updateStore(store: Store) {
        executeAction(store) { storesUseCases.updateStore(store) }
    }

    private fun executeAction(store: Store, block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
                _stateFlow.value = store
            } catch (e: StoresException) {
                _typeError.value = e.typeError
            }
        }
    }
}