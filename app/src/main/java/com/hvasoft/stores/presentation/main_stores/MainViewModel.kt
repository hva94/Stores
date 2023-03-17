package com.hvasoft.stores.presentation.main_stores

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
class MainViewModel @Inject constructor(
    private val storesUseCases: StoresUseCases
) : ViewModel() {

    private var _stateFlow = MutableStateFlow(listOf<Store>())
    val stateFlow: StateFlow<List<Store>> get() = _stateFlow

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var _typeError = MutableStateFlow(TypeError.NONE)
    val typeError: StateFlow<TypeError> get() = _typeError

    init {
        getStores()
    }

    private fun getStores() {
        executeAction {
            storesUseCases.getAllStores().collect { stores ->
                _stateFlow.value = stores
            }
        }
    }

    fun updateStore(store: Store) {
        store.isFavorite = !store.isFavorite
        executeAction { storesUseCases.updateStore(store) }
    }

    fun deleteStore(store: Store) {
        executeAction { storesUseCases.deleteStore(store) }
    }

    private fun executeAction(block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                block()
            } catch (e: StoresException) {
                _typeError.value = e.typeError
            } finally {
                _isLoading.value = false
            }
        }
    }
}