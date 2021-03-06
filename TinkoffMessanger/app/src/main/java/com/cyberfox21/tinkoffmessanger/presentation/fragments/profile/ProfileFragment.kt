package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentProfileBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import com.cyberfox21.tinkoffmessanger.presentation.common.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileEffect
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileEvent
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileState
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.enums.ProfileMode
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class ProfileFragment() : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private lateinit var screenMode: ProfileMode
    private lateinit var fragmentUser: User

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding = null")

    override val initEvent: ProfileEvent = ProfileEvent.Ui.GetCurrentUser

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
        configureToolbar()
        launchRightMode()
        addListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun createStore(): Store<ProfileEvent, ProfileEffect, ProfileState> =
        (activity as MainActivity).component.profileStore

    override fun render(state: ProfileState) {
        with(binding) {
            shimmerLayoutProfile.shimmerViewContainer.isVisible = state.isLoading
            emptyLayout.errorLayout.isVisible = state.isEmptyState && state.isLoading.not()
            binding.btnLogout.isVisible =
                screenMode == ProfileMode.YOUR &&
                        state.error == null &&
                        state.isEmptyState.not() &&
                        binding.shimmerLayoutProfile.shimmerViewContainer.isVisible.not()
            if (!state.isEmptyState && state.isLoading.not()) state.user?.let { bindUser(it) }
            errorLayout.errorRoot.isVisible = state.error != null && state.isLoading.not()
        }
    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.UserLoadError -> {
                binding.emptyLayout.errorLayout.isVisible = false
                binding.errorLayout.errorRoot.isVisible = true
            }
            is ProfileEffect.UserEmpty -> {
                binding.errorLayout.errorRoot.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = true
            }
            is ProfileEffect.UserPresenceLoadedSuccess -> {
                binding.tvProfileStatus.isVisible = true
                binding.tvProfileStatus.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        getStatusColor(effect.status)
                    )
                )
                binding.tvProfileStatus.text = effect.status.apiName
            }
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_MODE)) throw RuntimeException("Param mode is absent")
        val mode = args.get(EXTRA_MODE) as ProfileMode
        if (mode != ProfileMode.YOUR && mode != ProfileMode.STRANGER)
            throw RuntimeException("Unknown profile screen mode $mode")
        screenMode = mode
        store.currentState.profileScreenMode = mode
        if (screenMode == ProfileMode.STRANGER && !args.containsKey(EXTRA_USER))
            throw RuntimeException("Param user is absent")
        else if (screenMode == ProfileMode.STRANGER && args.containsKey(EXTRA_USER))
            fragmentUser = args.get(EXTRA_USER) as User
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_navigation_background)
    }

    private fun launchRightMode() = when (screenMode) {
        ProfileMode.YOUR -> {
            getCurrentUser()
            showBottomNavigation()
            binding.btnLogout.isVisible =
                screenMode == ProfileMode.YOUR &&
                        store.currentState.error == null &&
                        binding.shimmerLayoutProfile.shimmerViewContainer.isVisible.not()
        }
        ProfileMode.STRANGER -> {
            hideBottomNavigation()
            toolbarWithNavigation()
            binding.btnLogout.isVisible = false
            getSelectedUser()
        }
    }

    private fun hideBottomNavigation() {
        (activity as MainActivity).hideNavigation()
    }

    private fun showBottomNavigation() {
        (activity as MainActivity).showNavigation()
    }

    private fun getSelectedUser() {
        store.accept(ProfileEvent.Ui.GetSelectedUser(fragmentUser))
    }

    private fun getCurrentUser() {
        store.accept(ProfileEvent.Ui.GetCurrentUser)
    }

    private fun bindUser(user: User) {
        Glide.with(requireContext()).load(user.avatar).into(binding.ivProfilePhoto)
        binding.tvProfileName.text = user.name
    }

    private fun configureToolbar() {
        binding.toolbarLayout.toolbar.isVisible = true
        binding.toolbarLayout.toolbar.title = resources.getString(R.string.profile)
    }

    private fun toolbarWithNavigation() {
        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun addListeners() {
        binding.errorLayout.btnNetwork.setOnClickListener { launchRightMode() }
        binding.emptyLayout.btnRefresh.setOnClickListener { launchRightMode() }
    }

    private fun getStatusColor(status: UserStatus): Int = when (status) {
        UserStatus.IDLE -> R.color.orange
        UserStatus.ONLINE -> R.color.green
        UserStatus.OFFLINE -> R.color.light_gray
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

