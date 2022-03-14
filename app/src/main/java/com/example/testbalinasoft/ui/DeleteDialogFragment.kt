package com.example.testbalinasoft.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.testbalinasoft.network.checkForInternet
import com.example.testbalinasoft.viewmodel.DeleteDialogViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialogFragment : DialogFragment() {

    private val viewModel: DeleteDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Are you sure you want to delete this?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                if (checkForInternet(requireContext())) viewModel.onConfirmClick()
            }
            .create()
}