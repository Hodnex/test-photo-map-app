package com.example.testbalinasoft.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.testbalinasoft.R
import com.example.testbalinasoft.adapter.CommentAdapter
import com.example.testbalinasoft.data.Comment
import com.example.testbalinasoft.databinding.FragmentFullScreenBinding
import com.example.testbalinasoft.network.checkForInternet
import com.example.testbalinasoft.viewmodel.FullScreenViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class FullScreenFragment : Fragment(R.layout.fragment_full_screen),
    CommentAdapter.OnItemClickListener {

    private val viewModel: FullScreenViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFullScreenBinding.bind(view)

        commentAdapter = CommentAdapter(this)

        binding.apply {
            val image = viewModel.image
            Glide.with(requireContext()).load(image.url).into(imageViewFullPhoto)
            textViewDate.text = SimpleDateFormat("dd.MM.yyyy hh:mm").format(image.date * 1000L)
            recyclerViewComments.apply {
                adapter = commentAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            imageViewSendComment.setOnClickListener {
                if (checkForInternet(requireContext())) {
                    val text = editTextComment.text.toString()
                    editTextComment.setText("")
                    viewModel.postComment(text)
                }
            }
        }

        if (checkForInternet(requireContext())) viewModel.getComments()
        else {
            viewModel.showErrorMessage("Lost internet connection")
        }

        val listener = activity as MainActivity
        listener.hideFab()

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.fullScreenEvent.collect { event ->
                when (event) {
                    is FullScreenViewModel.FullScreenEvent.NavigateToDeleteDialog -> {
                        val action =
                            FullScreenFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment(
                                comment = event.comment
                            )
                        findNavController().navigate(action)
                    }
                    is FullScreenViewModel.FullScreenEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.comments.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it)
        }
    }

    override fun onItemLongClick(comment: Comment) {
        viewModel.deleteComment(comment)
    }
}