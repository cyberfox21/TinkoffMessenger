package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.content.Context
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
import com.cyberfox21.tinkoffmessanger.presentation.MainActivity
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.*
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ProfileFragment() : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private lateinit var screenMode: ProfileMode
    private lateinit var fragmentUser: User

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding = null")

//  < ---------------------------------------- ELM --------------------------------------------->

    @Inject
    internal lateinit var actor: ProfileActor

    override val initEvent: ProfileEvent = ProfileEvent.Ui.GetCurrentUser

    override fun createStore(): Store<ProfileEvent, ProfileEffect, ProfileState> =
        ProfileStoreFactory(actor).provide()

    override fun render(state: ProfileState) {
        with(binding) {
            shimmerLayoutProfile.shimmerViewContainer.isVisible = state.isLoading
            emptyLayout.errorLayout.isVisible = state.isEmptyState
            binding.btnLogout.isVisible =
                screenMode == ProfileMode.YOUR &&
                        state.error == null &&
                        binding.shimmerLayoutProfile.shimmerViewContainer.isVisible.not()
            if (!state.isEmptyState) state.user?.let { bindUser(it) }
            networkErrorLayout.errorLayout.isVisible = state.error != null
        }
    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.UserLoadError -> {
                binding.emptyLayout.errorLayout.isVisible = false
                binding.networkErrorLayout.errorLayout.isVisible = true
            }
            is ProfileEffect.UserEmpty -> {
                binding.networkErrorLayout.errorLayout.isVisible = false
                binding.emptyLayout.errorLayout.isVisible = true
            }
        }
    }

//  < ---------------------------------------- ELM --------------------------------------------->

    override fun onAttach(context: Context) {
        (activity as MainActivity).component.injectProfileFragment(this)
        super.onAttach(context)
    }

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
        addListeners()
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

    private fun launchRightMode() = when (screenMode) {
        ProfileMode.YOUR -> {
            store.accept(ProfileEvent.Ui.GetCurrentUser)
            binding.btnLogout.isVisible =
                screenMode == ProfileMode.YOUR &&
                        store.currentState.error == null &&
                        binding.shimmerLayoutProfile.shimmerViewContainer.isVisible.not()
        }
        ProfileMode.STRANGER -> {
            configureToolbar()
            binding.btnLogout.isVisible = false
            store.accept(ProfileEvent.Ui.GetSelectedUser(fragmentUser))
        }
    }

    private fun bindUser(user: User) {
        Glide.with(requireContext()).load(user.avatar).into(binding.ivProfilePhoto)
        binding.tvProfileName.text = user.name
    }

    private fun configureToolbar() {
        binding.toolbarLayout.toolbar.isVisible = true
        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarLayout.toolbar.title = resources.getString(R.string.profile)
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun addListeners() {
        binding.networkErrorLayout.networkButton.setOnClickListener { launchRightMode() }
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
