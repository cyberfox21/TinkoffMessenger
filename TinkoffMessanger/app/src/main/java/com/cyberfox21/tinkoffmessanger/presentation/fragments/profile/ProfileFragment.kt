package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var screenMode: ProfileMode

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding = null")

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchRightMode()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_MODE)) throw RuntimeException("Param mode is absent")
        val mode = args.get(EXTRA_MODE) as ProfileMode
        if (mode != ProfileMode.YOUR && mode != ProfileMode.STRANGER)
            throw RuntimeException("Unknown profile screen mode $mode")
        screenMode = mode
    }

    private fun launchRightMode() {
        if (screenMode == ProfileMode.STRANGER) {
            binding.btnLogout.visibility = View.GONE
        }
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.userLD.observe(viewLifecycleOwner) {
            processProfileScreenState(it)
        }
    }

    private fun processProfileScreenState(state: ProfileScreenState) = when (state) {
        is ProfileScreenState.Result -> {
            binding.pbLoading.isVisible = false
            Glide.with(requireContext()).load(state.user.avatar).into(binding.ivProfilePhoto)
            binding.tvProfileName.text = state.user.name
            //binding.tvProfileStatus.
        }
        ProfileScreenState.Loading -> {
            binding.pbLoading.isVisible = true
        }
        is ProfileScreenState.Error -> {
            binding.pbLoading.isVisible = false
            Toast.makeText(this.context, "${state.error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        private const val EXTRA_MODE = "extra_mode"

        fun newInstance(mode: ProfileMode): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_MODE, mode)
                }
            }
        }
    }
}
