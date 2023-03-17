package com.hvasoft.stores.presentation.main_stores

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hvasoft.stores.R
import com.hvasoft.stores.core.Utils.showMessage
import com.hvasoft.stores.core.Utils.startIntent
import com.hvasoft.stores.core.exceptions.TypeError
import com.hvasoft.stores.databinding.FragmentMainBinding
import com.hvasoft.stores.domain.model.Store
import com.hvasoft.stores.presentation.main_stores.adapter.OnClickListener
import com.hvasoft.stores.presentation.main_stores.adapter.StoresAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), OnClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoresAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        binding.floatingButton.setOnClickListener { startEditFragment() }
    }

    private fun setupRecyclerView() {
        adapter = StoresAdapter(this)
        val gridLayoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.main_columns))

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = this@MainFragment.adapter
        }
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.stateFlow.collectLatest { storeList ->
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(storeList)
                }
                viewModel.isLoading.collectLatest {
                    binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
                }
                viewModel.typeError.collectLatest { typeError ->
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

    private fun startEditFragment(store: Store = Store()) {
        val action = MainFragmentDirections.actionMainFragmentToEditStoreFragment(store)
        findNavController().navigate(action)
    }

    private fun confirmDelete(store: Store) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                    viewModel.deleteStore(store)
                }
                .setNegativeButton(R.string.dialog_delete_cancel, null)
                .show()
        }
    }

    private fun dialPhone(phone: String) {
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    private fun goToWebsite(website: String) {
        if (website.isBlank()) {
            showMessage(R.string.main_error_no_website, true)
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * OnClickListener
     */
    override fun onClick(store: Store) {
        startEditFragment(store)
    }

    override fun onLongClick(store: Store) {
        val items = resources.getStringArray(R.array.array_options_item)

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_options_title)
                .setItems(items) { _, index ->
                    when (index) {
                        0 -> confirmDelete(store)

                        1 -> dialPhone(store.phone)

                        2 -> goToWebsite(store.website)
                    }
                }
                .show()
        }
    }

    override fun onFavoriteClick(store: Store) {
        viewModel.updateStore(store)
    }

}