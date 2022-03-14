package com.example.testbalinasoft.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testbalinasoft.R
import com.example.testbalinasoft.adapter.ImageAdapter
import com.example.testbalinasoft.data.Image
import com.example.testbalinasoft.databinding.FragmentPhotosBinding
import com.example.testbalinasoft.viewmodel.PhotosViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment(R.layout.fragment_photos), ImageAdapter.OnItemClickListener {

    private val viewModel: PhotosViewModel by viewModels()

    private lateinit var binding : FragmentPhotosBinding
    private lateinit var imageAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPhotosBinding.bind(view)

        imageAdapter = ImageAdapter(this, requireContext())

        binding.apply {
            recyclerViewPhotos.apply {
                adapter = imageAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
            }
        }

        val listener = activity as MainActivity
        listener.showFab()

        viewModel.downloadImages()

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.photosEvent.collect(){ event ->
                when(event) {
                    is PhotosViewModel.PhotosEvent.NavigateToFullScreen -> {
                        val action = PhotosFragmentDirections.actionPhotosFragmentToFullScreenFragment(event.image)
                        findNavController().navigate(action)
                    }
                    is PhotosViewModel.PhotosEvent.NavigateToDeleteDialog -> {
                        val action = PhotosFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment(image = event.image)
                        findNavController().navigate(action)
                    }
                    is PhotosViewModel.PhotosEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.submitList(it)
        }
    }

    override fun onItemLongClick(image: Image) {
        viewModel.deleteImage(image)
    }

    override fun onItemClick(image: Image) {
        viewModel.onItemClick(image)
    }
}