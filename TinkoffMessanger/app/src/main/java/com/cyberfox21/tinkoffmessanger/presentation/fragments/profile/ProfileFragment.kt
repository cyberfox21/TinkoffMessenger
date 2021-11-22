package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentProfileBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.presentation.NavigationHolder

class ProfileFragment : Fragment() {

    private lateinit var screenMode: ProfileMode
    private lateinit var fragmentUser: User

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
        setupStatusBar()
        launchRightMode()
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
        if (screenMode == ProfileMode.STRANGER && !args.containsKey(EXTRA_USER))
            throw RuntimeException("Param user is absent")
        else if (screenMode == ProfileMode.STRANGER && args.containsKey(EXTRA_USER))
            fragmentUser = args.get(EXTRA_USER) as User
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_navigation_background)
    }

    private fun launchRightMode() {
        if (screenMode == ProfileMode.STRANGER) {
            configureToolbar()
            binding.btnLogout.visibility = View.GONE
            bindUser(fragmentUser)
        } else {
            binding.toolbarLayout.toolbar.isVisible = false
            (activity as NavigationHolder).showNavigation()
            setupViewModel()
        }
    }

    private fun configureToolbar() {
        binding.toolbarLayout.toolbar.isVisible = true
        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarLayout.toolbar.title = resources.getString(R.string.profile)
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.start()
        profileViewModel.userLD.observe(viewLifecycleOwner) {
            processProfileScreenState(it)
        }
    }

    private fun processProfileScreenState(state: ProfileScreenState) = when (state) {
        is ProfileScreenState.Result -> {
            binding.pbLoading.isVisible = false
            bindUser(state.user)
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

    private fun bindUser(user: User) {
        Glide.with(requireContext()).load(user.avatar).into(binding.ivProfilePhoto)
        binding.tvProfileName.text = user.name
    }

    companion object {

        const val PROFILE_FRAGMENT_NAME = "profile_fragment"

        private const val EXTRA_MODE = "extra_mode"

        private const val EXTRA_USER = "user"

        fun newInstanceYour(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_MODE, ProfileMode.YOUR)
                }
            }
        }

        fun newInstanceStranger(user: User): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_MODE, ProfileMode.STRANGER)
                    putParcelable(EXTRA_USER, user)
                }
            }
        }
    }
}
