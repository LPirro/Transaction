package com.lpirro.transaction.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lpirro.transaction.base.BaseFragment
import com.lpirro.transaction.extensions.hideKeyboard
import com.lpirro.transaction.presentation.adapter.CommandAdapter
import com.lpirro.transaction.viewmodel.TransactionViewModel
import com.lpirro.transaction.viewmodel.TransactionViewModel.TransactionUiState
import com.lpirro.transactionscreen.databinding.FragmentTransactionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var commandAdapter: CommandAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTransactionBinding
        get() = FragmentTransactionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservers()
        setupRecyclerView()

        binding.commandBuilderView.sendCommandListener = ::sendCommand
    }

    private fun sendCommand() {
        val command = binding.commandBuilderView.commandInput
        viewModel.validateCommandProtection(command)
        hideKeyboard()
    }

    private fun onUiUpdate(uiState: TransactionUiState) {
        if (uiState.commandProtectionDialog) {
            showCommandProtectionDialog()
        }

        binding.commandBuilderView.commandInput = uiState.commandInput
        commandAdapter.submitList(uiState.commandList.toMutableList())
    }

    private fun showCommandProtectionDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure to perform this operation?")
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            viewModel.executeTransaction(binding.commandBuilderView.commandInput)
            dialog.dismiss()
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setupRecyclerView() {
        commandAdapter = CommandAdapter()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.commandRecyclerView.layoutManager = linearLayoutManager
        binding.commandRecyclerView.adapter = commandAdapter
    }

    private fun registerObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch { viewModel.uiState.collect(::onUiUpdate) }
            }
        }
    }
}
