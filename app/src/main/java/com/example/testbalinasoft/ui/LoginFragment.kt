package com.example.testbalinasoft.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testbalinasoft.R
import com.example.testbalinasoft.databinding.FragmentLoginBinding
import com.example.testbalinasoft.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)

        binding.apply {

            buttonSingUp.setOnClickListener {
                val login = editTextLogin.text.toString()
                val password = editTextPassword.text.toString()
                val confirmPassword = editTextPasswordConfirm.text.toString()
                viewModel.signInCheck(login, password, confirmPassword)
            }

            buttonLogIn.setOnClickListener {
                val login = editTextLogin.text.toString()
                val password = editTextPassword.text.toString()
                viewModel.loginCheck(login, password)
            }

            tabLayoutLogin.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab!!.text) {
                        "Login" -> {
                            editTextPasswordConfirm.visibility = View.INVISIBLE
                            buttonSingUp.visibility = View.INVISIBLE
                            buttonLogIn.visibility = View.VISIBLE
                        }
                        "Register" -> {
                            editTextPasswordConfirm.visibility = View.VISIBLE
                            buttonSingUp.visibility = View.VISIBLE
                            buttonLogIn.visibility = View.INVISIBLE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        val listener = activity as MainActivity
        listener.hideFab()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewModel.LoginEvent.NavigateToPhotoScreen -> {
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToPhotosFragment()
                        findNavController().navigate(action)
                    }
                    is LoginViewModel.LoginEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG ).show()
                    }
                }
            }
        }
    }
}