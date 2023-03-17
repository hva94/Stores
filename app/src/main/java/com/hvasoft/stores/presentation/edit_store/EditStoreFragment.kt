package com.hvasoft.stores.presentation.edit_store

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hvasoft.stores.R
import com.hvasoft.stores.core.Utils.editable
import com.hvasoft.stores.core.Utils.hideKeyboard
import com.hvasoft.stores.core.Utils.loadImage
import com.hvasoft.stores.core.Utils.showMessage
import com.hvasoft.stores.core.exceptions.TypeError
import com.hvasoft.stores.databinding.FragmentEditStoreBinding
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.domain.utils.Validations.validateFields
import com.hvasoft.stores.presentation.HostActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "EditStoreFragment"

@AndroidEntryPoint
class EditStoreFragment : Fragment() {

    private var _binding: FragmentEditStoreBinding? = null
    private val binding get() = _binding!!

    private var _hostActivity: HostActivity? = null
    private val hostActivity get() = _hostActivity!!

    private val viewModel: EditStoreViewModel by viewModels()
    private val args by navArgs<EditStoreFragmentArgs>()
    private var isEditMode: Boolean = false
    private lateinit var currentStore: Store

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        showMessage(R.string.main_error_unknown, true)
        Log.e(TAG, "Coroutine exception", throwable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupViewModel()
        setupTextFields()
    }

    private fun setupMenu() {
        _hostActivity = activity as HostActivity

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEditMode) {
                    showAlertDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
        hostActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        hostActivity.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_save, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        hostActivity.onBackPressedDispatcher.onBackPressed()
                        true
                    }
                    R.id.action_save -> {
                        if (validateFields(
                                binding.tilPhotoUrl,
                                binding.tilPhone,
                                binding.tilName
                            )
                        ) {
                            currentStore.apply {
                                name = binding.etName.text.toString().trim()
                                phone = binding.etPhone.text.toString().trim()
                                website = binding.etWebsite.text.toString().trim()
                                photoUrl = binding.etPhotoUrl.text.toString().trim()
                            }
                            if (isEditMode) viewModel.updateStore(currentStore)
                            else viewModel.insertStore(currentStore)
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getStoreSelected(args.currentStore).collectLatest { store ->
                    currentStore = store ?: Store()
                    if (store != null) {
                        isEditMode = true
                        setUiStore(store)
                    } else {
                        isEditMode = false
                    }
                    setupActionBar()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest { result ->
                    hideKeyboard()
                    when (result) {
                        is Store -> {
                            val msgRes =
                                if (result.id == 0L) R.string.edit_store_message_save_success
                                else R.string.edit_store_message_update_success
                            showMessage(msgRes)
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.typeError.collectLatest { typeError ->
                    if (typeError != TypeError.NONE) {
                        val msgRes = when (typeError) {
                            TypeError.GET -> R.string.main_error_get
                            TypeError.INSERT -> R.string.main_error_insert
                            TypeError.UPDATE -> R.string.main_error_update
                            TypeError.DELETE -> R.string.main_error_delete
                            else -> R.string.main_error_unknown
                        }
                        showMessage(msgRes, true)
                    }
                }
            }
        }
    }

    private fun setupTextFields() {
        binding.apply {
            etName.addTextChangedListener { validateFields(tilName) }
            etPhone.addTextChangedListener { validateFields(tilPhone) }
            etPhotoUrl.addTextChangedListener {
                validateFields(tilPhotoUrl)
                imgPhoto.loadImage(it.toString().trim())
            }
        }
    }

    private fun showAlertDialog() {
        hostActivity.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_exit_title)
                .setMessage(R.string.dialog_exit_message)
                .setPositiveButton(R.string.dialog_exit_ok) { _, _ ->
                    findNavController().navigateUp()
                }
                .setNegativeButton(R.string.dialog_delete_cancel, null)
                .show()
        }
    }

    private fun setupActionBar() {
        hostActivity.supportActionBar?.title =
            if (isEditMode) getString(R.string.edit_store_title_edit)
            else getString(R.string.edit_store_title_add)
    }

    private fun setUiStore(store: Store) {
        binding.apply {
            etName.text = store.name.editable()
            etPhone.text = store.phone.editable()
            etWebsite.text = store.website.editable()
            etPhotoUrl.text = store.photoUrl.editable()
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        hostActivity.supportActionBar?.title = getString(R.string.app_name)
        _hostActivity = null
        _binding = null
        super.onDestroyView()
    }
}